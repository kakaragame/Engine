package org.kakara.engine.models;

import org.joml.Vector4f;
import org.kakara.engine.GameEngine;
import org.kakara.engine.exceptions.ModelLoadException;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.resources.FileResource;
import org.kakara.engine.resources.JarResource;
import org.kakara.engine.resources.Resource;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.utils.RGBA;
import org.kakara.engine.utils.Utils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * A model loader for static Models
 */
public class StaticModelLoader {
    private StaticModelLoader() {

    }

    /**
     * Load a model
     *
     * @param resource        The resource for the model file (.obj)
     * @param texturesDir     The location of the texture directory
     * @param scene           The current scene
     * @param resourceManager The resource manager.
     * @return The Array of meshes.
     * @throws Exception
     */
    public static Mesh[] load(Resource resource, String texturesDir, Scene scene, ResourceManager resourceManager) throws Exception {
        return load(resource, texturesDir, resourceManager, scene, aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices | aiProcess_Triangulate
                | aiProcess_FixInfacingNormals);
    }

    /**
     * Loads a model
     *
     * @param resource        The resource for the model file
     * @param texturesDir     The location of the Texture directory
     * @param resourceManager The resource manager
     * @param scene           The current scene
     * @param flags           Flags to load the object by.
     * @return The array of meshes.
     * @throws Exception
     */
    public static Mesh[] load(Resource resource, String texturesDir, ResourceManager resourceManager, Scene scene, int flags) throws Exception {
        GameEngine.LOGGER.debug(String.format("Loading Model %s With Textures in %s", resource.toString(), texturesDir));

        AIScene aiScene = null;
        if (resource instanceof FileResource) {
            aiScene = aiImportFile(resource.getPath(), flags);
        } else if (resource instanceof JarResource) {
            AIFileIO fileIo = AIFileIO.create();
            AIFileOpenProcI fileOpenProc = new SimpleAIFileOpenProc();
            AIFileCloseProcI fileCloseProc = new SimpleAIFileCloseProc();
            fileIo.set(fileOpenProc, fileCloseProc, NULL);
            aiScene = aiImportFileEx(resource.getPath(), flags, fileIo);
        }
        //I feel like this is gonna be a problem
        if (aiScene == null) {
            throw new ModelLoadException(aiGetErrorString());
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials, texturesDir, resourceManager, scene);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh, materials);
            meshes[i] = mesh;
        }

        return meshes;
    }

    protected static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
    }

    protected static void processMaterial(AIMaterial aiMaterial, List<Material> materials,
                                          String texturesDir, ResourceManager resourceManager, Scene scene) throws Exception {
        // File.separator. File.pathSeparator is for the PATH variable.
        String separator = "/";
        AIColor4D colour = AIColor4D.create();
        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null,
                null, null, null, null, null);
        String textPath = path.dataString();
        Texture texture = null;
        if (textPath != null && textPath.length() > 0) {
            TextureCache textCache = TextureCache.getInstance(resourceManager);
            String textureFile = texturesDir + separator + textPath;
            textureFile = textureFile.replace("//", separator);
            GameEngine.LOGGER.debug(String.format("Getting Texture from %s", textureFile));
            texture = textCache.getTexture(textureFile, scene);
        }

//            int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0,
//                    colour);


        RGBA specular = Material.DEFAULT_COLOUR;
        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0,
                colour);
        if (result == 0) {
            specular = new RGBA(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Material material = new Material(specular, 1.0f);
        material.setTexture(texture);
        materials.add(material);
    }

    private static Mesh processMesh(AIMesh aiMesh, List<Material> materials) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList();

        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        Mesh mesh = new Mesh(Utils.listToArray(vertices), Utils.listToArray(textures),
                Utils.listToArray(normals), Utils.listIntToArray(indices));
        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new Material();
        }
        mesh.setMaterial(material);

        return mesh;
    }

    protected static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        while (aiNormals != null && aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }

    protected static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
        AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
        int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
        for (int i = 0; i < numTextCoords; i++) {
            AIVector3D textCoord = textCoords.get();
            textures.add(textCoord.x());
            textures.add(1 - textCoord.y());
        }
    }

    protected static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }
}
