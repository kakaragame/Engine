package org.kakara.engine.test;

import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.components.MeshRenderer;
import org.kakara.engine.components.Transform;
import org.kakara.engine.debug.DebugCanvas;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.exceptions.ModelLoadException;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.gameitems.mesh.AtlasMesh;
import org.kakara.engine.gameitems.mesh.InstancedMesh;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.gameitems.particles.FlowParticleEmitter;
import org.kakara.engine.gameitems.particles.Particle;
import org.kakara.engine.input.Input;
import org.kakara.engine.input.controller.GamePadButtonEvent;
import org.kakara.engine.input.controller.ids.ControllerID;
import org.kakara.engine.input.controller.ids.GamePadAxis;
import org.kakara.engine.input.controller.ids.GamePadButton;
import org.kakara.engine.input.key.KeyCode;
import org.kakara.engine.input.key.KeyInput;
import org.kakara.engine.input.mouse.MouseClickType;
import org.kakara.engine.input.mouse.MouseInput;
import org.kakara.engine.lighting.DirectionalLight;
import org.kakara.engine.lighting.LightColor;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.models.StaticModelLoader;
import org.kakara.engine.physics.collision.BoxCollider;
import org.kakara.engine.physics.collision.ColliderComponent;
import org.kakara.engine.physics.collision.PhysicsComponent;
import org.kakara.engine.physics.collision.VoxelCollider;
import org.kakara.engine.voxels.Voxel;
import org.kakara.engine.voxels.VoxelChunk;
import org.kakara.engine.voxels.VoxelTexture;
import org.kakara.engine.voxels.TextureAtlas;
import org.kakara.engine.voxels.mesh.MeshType;
import org.kakara.engine.voxels.layouts.BlockLayout;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.AbstractGameScene;
import org.kakara.engine.test.components.PlayerMovement;
import org.kakara.engine.ui.components.shapes.Rectangle;
import org.kakara.engine.ui.components.text.Text;
import org.kakara.engine.ui.font.Font;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.items.ObjectCanvas;
import org.kakara.engine.ui.objectcanvas.UIObject;
import org.kakara.engine.utils.RGBA;
import org.kakara.engine.utils.Time;
import org.kakara.engine.utils.Utils;
import org.kakara.engine.weather.Fog;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MainGameScene extends AbstractGameScene {
    private GameItem player;
    private GameHandler handler;
    private KakaraTest test;
    private boolean stopped = false;

    private FlowParticleEmitter particleEmitter;

    private float lightAngle;

    private Text fps;

    public MainGameScene(GameHandler gameHandler, KakaraTest test) throws Exception {
        super(gameHandler);

        this.test = test;

    }

    public GameItem getPlayer() {
        return player;
    }

    @Override
    public void work() {

    }

    @Override
    public void loadGraphics(GameHandler gameHandler) throws Exception {
        try {
            System.out.println("Started Loading");
            long time = System.currentTimeMillis();
            lightAngle = 45;

            // Remove the Cursor
            setCursorStatus(false);
            getCamera().setPosition(0, 3, 0);
            // Load a test model.
            var resourceManager = gameHandler.getResourceManager();
            Mesh[] mainPlayer = StaticModelLoader.load(resourceManager.getResource("player/steve.obj"), "/player", this, resourceManager);
            GameItem object = new GameItem(mainPlayer);
            object.transform.setPosition(0, 20, 0);
            object.transform.setScale(0.3f, 0.3f, 0.3f);
            object.getMeshRenderer().get().getMesh().setWireframe(true);

            add(object);
            player = object;

            // Load an instance mesh
            InstancedMesh mesh = new InstancedMesh(CubeData.vertex, CubeData.texture, CubeData.normal, CubeData.indices, 10000);
            InputStream io = Texture.class.getResourceAsStream("/example_texture.png");
            Texture grass = Utils.inputStreamToTexture(io);
            Material mt = new Material(grass);

            mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));
            mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/ovly2.png")));
            mt.setReflectance(0.3f);
            mesh.setMaterial(mt);
            Mesh me = new Mesh(CubeData.vertex, CubeData.texture, CubeData.normal, CubeData.indices);
            me.setMaterial(mt);

            // Create a new Game Item while testing Components
            GameItem gi = new GameItem(mesh);
            gi.addComponent(BoxCollider.class);
            System.out.println("TEST:: " + gi.getComponent(ColliderComponent.class));
            System.out.println(Transform.class.isAssignableFrom(MeshRenderer.class));
            add(gi);
            gi.transform.setPosition(3, 16 * 2 + 5, 3);

            PhysicsComponent physicsComponent = gi.addComponent(PhysicsComponent.class);
            // Add a component to handle player movement.
            gi.addComponent(PlayerMovement.class);
            physicsComponent.setVelocity(new Vector3(0, -9.18f, 0));

            // Create another game item that can be removed.
            GameItem gi2 = new GameItem(mesh);
            add(gi2);
            gi2.transform.setPosition(6, 16 * 2, 6);
            BoxCollider bc = gi2.addComponent(BoxCollider.class);
            bc.setTrigger(true);

            gi2.setTag("Test");

            // Tell the box to remove itself upon collision enter using the old system.
            // The component system should now be used.
            gi.getComponent(BoxCollider.class).addOnTriggerEnter((ColliderComponent other) -> {
                if (other instanceof VoxelCollider) return;
                if (other.getGameItem().getTag().equals("Test")) {
                    remove(gi2);
                }
            });

        /*

        ==================================================
                       Render Chunk
        ===================================================

         */
            VoxelTexture txt1 = new VoxelTexture(resourceManager.getResource("/example_texture.png"));
            VoxelTexture txt2 = new VoxelTexture(resourceManager.getResource("/oop.png"));
            VoxelTexture txt3 = new VoxelTexture(resourceManager.getResource("/ExampleBlock.png"));
            VoxelTexture txt5 = new VoxelTexture(resourceManager.getResource("/ovly2.png"));
            VoxelTexture txt6 = new VoxelTexture(resourceManager.getResource("/fewio.png"));
            TextureAtlas atlas = new TextureAtlas(Arrays.asList(txt1, txt2, txt3, txt5, txt6), Paths.get("").toAbsolutePath().toString(), this);
            setTextureAtlas(atlas);

            // Generate Render Chunks in a new thread.
            new Thread(() -> {
                for (int cx = 0; cx < 7; cx++) {
                    for (int cy = 0; cy < 2; cy++) {
                        for (int cz = 0; cz < 7; cz++) {
                            VoxelChunk rc = new VoxelChunk(new ArrayList<>(), getTextureAtlas());
                            rc.transform.setPosition(cx * 16, cy * 16, cz * 16);
                            for (int x = 0; x < 16; x++) {
                                for (int y = 0; y < 16; y++) {
                                    for (int z = 0; z < 16; z++) {
                                        Voxel rb;
                                        if (x % 3 == 0) {
                                            rb = new Voxel(new BlockLayout(), getTextureAtlas().getTextures().get(4), new Vector3(x, y, z));
                                            rb.setOpaque(false);
                                        } else {
                                            rb = new Voxel(new BlockLayout(), getTextureAtlas().getTextures().get(ThreadLocalRandom.current().nextInt(0, 3)), new Vector3(x, y, z));
                                        }
                                        if (x % 2 == 0) {
                                            rb.setOverlay(getTextureAtlas().getTextures().get(3));
                                        }

                                        rc.addVoxel(rb);
                                    }
                                }
                            }

                            rc.regenerateChunk(getTextureAtlas(), MeshType.MULTITHREAD);

                            getChunkHandler().addChunk(rc);
                        }
                    }
                }
            }).start();


            // Add a cloned game item.
            GameItem sh = gi.clone(false);
            sh.transform.setPosition(-4, 3, -4);
            this.add(sh);


            // Add a point light.
            PointLight pointLight = new PointLight(new LightColor(0, 255, 255), new Vector3(5, 16 * 2 + 1, 5), 1);
            PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
            pointLight.setAttenuation(att);
            this.add(pointLight);


            // Setup the directional light
            DirectionalLight directionalLight = new DirectionalLight(new LightColor(255, 255, 255), new Vector3(0, 1, 0.5f), 0.5f);
            directionalLight.setShadowPosMult(8);
            directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
            assert getLightHandler() != null;
            getLightHandler().setDirectionalLight(directionalLight);


            // Set the fog for the level.
            setFog(new Fog(true, new RGBA(255, 255, 255), 0.005f));


            setupCanvas(txt2);

            setupParticles();

            this.handler = gameHandler;

            add(new DebugCanvas());

            System.out.println("Done. Scene loaded in " + (System.currentTimeMillis() - time) + " ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(float interval) {
        KeyInput ki = handler.getKeyInput();

        // Set the FPS text.
        fps.setText("FPS: " + Math.round(1 / Time.getDeltaTime()));

        // Use controller input if possible.
        if (getControllerManager().controllerExists(ControllerID.CONTROLLER_ONE)) {
            // Primary camera movement.
            getCamera().movePosition(0, 0, Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.LEFT_STICK_Y));
            getCamera().movePosition(Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.LEFT_STICK_X), 0, 0);
            getCamera().moveRotation(2.5f * Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.RIGHT_STICK_Y),
                    2.5f * Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.RIGHT_STICK_X), 0);

            // Up and down.
            if (Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.A)) {
                getCamera().movePosition(0, 1, 0);
            }
            if (Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.B)) {
                getCamera().movePosition(0, -1, 0);
            }
            // Exit the game.
            if (Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.START))
                test.exit();
        }

        if (ki.isKeyPressed(KeyCode.W)) {
            getCamera().movePosition(0, 0, -1);
        }
        if (ki.isKeyPressed(KeyCode.S)) {
            getCamera().movePosition(0, 0, 1);
        }
        if (ki.isKeyPressed(KeyCode.A)) {
            getCamera().movePosition(-1, 0, 0);
        }
        if (ki.isKeyPressed(KeyCode.D)) {
            getCamera().movePosition(1, 0, 0);
        }
        if (ki.isKeyPressed(KeyCode.SPACE)) {
            getCamera().movePosition(0, 1, 0);
        }
        if (ki.isKeyPressed(KeyCode.LEFT_SHIFT)) {
            getCamera().movePosition(0, -1, 0);
        }
        if (ki.isKeyPressed(KeyCode.ESCAPE)) {
            test.exit();
        }
        if (ki.isKeyPressed(KeyCode.TAB)) {
            this.setCursorStatus(true);
            stopped = !stopped;
        }

        // Camera Movement with the mouse.
        MouseInput mi = handler.getMouseInput();
        if (!stopped)
            getCamera().moveRotation((float) (mi.getDeltaPosition().y), (float) mi.getDeltaPosition().x, 0);
        if (handler.getSoundManager().getListener() != null)
            handler.getSoundManager().getListener().setPosition(getCamera().getPosition());

        // Change the light angle.
        lightAngle += 3;
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = getLightHandler().getDirectionalLight().getDirection().toJoml();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();
//        getLightHandler().getDirectionalLight().setDirection(new Vector3(lightDirection));

        particleEmitter.update((long) (interval * 1000));
    }

    @EventHandler
    public void OnMouseClick(MouseClickEvent evt) {
        if (evt.getMouseClickType() == MouseClickType.LEFT_CLICK) {
            ColliderComponent selected = this.selectGameItems(20);
            System.out.println(selected);
            if (selected instanceof VoxelCollider) {
                System.out.println("Clicked!");
                Voxel block = ((VoxelCollider) selected).getVoxel();
                VoxelChunk parentChunk = block.getParentChunk();
                parentChunk.removeVoxel(block);
                parentChunk.regenerateChunk(getTextureAtlas(), MeshType.SYNC);
            }
        }
    }

    @EventHandler
    public void onButtonClick(GamePadButtonEvent event) {
        if (event.getButtonID() == GamePadButton.RIGHT_BUMPER) {
            ColliderComponent selected = this.selectGameItems(20);
            System.out.println(selected);
            if (selected instanceof VoxelCollider) {
                System.out.println("Clicked!");
                Voxel block = ((VoxelCollider) selected).getVoxel();
                VoxelChunk parentChunk = block.getParentChunk();
                parentChunk.removeVoxel(block);
                parentChunk.regenerateChunk(getTextureAtlas(), MeshType.SYNC);
            }
        }
    }

    private void setupCanvas(VoxelTexture txt2) {
        ComponentCanvas cc = new ComponentCanvas(this);
        Font font = new Font("Roboto-Regular", gameHandler.getResourceManager().getResource("Roboto-Regular.ttf"), this);
        userInterface.addFont(font);

        Text fps = new Text("FPS: 000", font);
        fps.setColor(new RGBA(255, 255, 255, 1));

        fps.setPosition(20, 20);
        cc.add(fps);
        this.fps = fps;
        Rectangle rect = new Rectangle();
        rect.setColor(new RGBA(0, 255, 0, 1));
        rect.setScale(5, 5);
        rect.setPosition((float) gameHandler.getWindow().getWidth() / 2, (float) gameHandler.getWindow().getHeight() / 2);
        cc.add(rect);

        add(cc);

        ObjectCanvas oc = new ObjectCanvas(this);
        AtlasMesh m = new AtlasMesh(txt2, getTextureAtlas(), new BlockLayout(), CubeData.vertex, CubeData.normal, CubeData.indices);

        UIObject ui = new UIObject(m);
        ui.setPosition((float) 200, (float) 200);
        ui.setScale(100);
        ui.getRotation().rotateX((float) Math.toRadians(50));
        ui.getRotation().rotateY((float) Math.toRadians(40));
        oc.add(ui);
        add(oc);
    }

    private void setupParticles() throws ModelLoadException {
        ResourceManager resourceManager = gameHandler.getResourceManager();
        int maxParticles = 200;
        Vector3f particleSpeed = new Vector3f(0, 1, 0);
        particleSpeed.mul(2.5f);
        long ttl = 4000;
        long creationPeriodMillis = 300;
        float range = 0.2f;
        float scale = 1.0f;
//        Mesh partMesh = OBJLoader.loadMesh("/models/particle.obj", maxParticles);
        Mesh partMesh = StaticModelLoader.load(resourceManager.getResource("particle.obj"), "", this, resourceManager)[0];
        Texture particleTexture = new Texture(resourceManager.getResource("particle_anim.png"), 4, 4, this);
        Material partMaterial = new Material(particleTexture, 1);
        partMesh.setMaterial(partMaterial);
        Particle particle = new Particle(partMesh, new Vector3(particleSpeed), ttl, 100);
        particle.transform.setScale(scale, scale, scale);
        particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
        particleEmitter.setActive(true);
        particleEmitter.setPositionRndRange(range);
        particleEmitter.setSpeedRndRange(range);
        particleEmitter.setAnimRange(10);
        this.add(particleEmitter);
    }


}
