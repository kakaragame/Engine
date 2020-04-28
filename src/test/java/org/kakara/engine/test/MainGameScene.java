package org.kakara.engine.test;

import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.collision.Collidable;
import org.kakara.engine.collision.ObjectBoxCollider;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.input.KeyInput;
import org.kakara.engine.input.MouseClickType;
import org.kakara.engine.input.MouseInput;
import org.kakara.engine.item.*;
import org.kakara.engine.item.particles.FlowParticleEmitter;
import org.kakara.engine.item.particles.Particle;
import org.kakara.engine.lighting.DirectionalLight;
import org.kakara.engine.lighting.LightColor;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.models.StaticModelLoader;
import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.renderobjects.RenderTexture;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.renderlayouts.BlockLayout;
import org.kakara.engine.scene.AbstractGameScene;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.components.Rectangle;
import org.kakara.engine.ui.components.Text;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.text.Font;
import org.kakara.engine.utils.Time;
import org.kakara.engine.utils.Utils;
import org.kakara.engine.weather.Fog;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameScene extends AbstractGameScene {
    private GameItem player;
    private GameHandler handler;
    private GameItem collider;
    private KakaraTest test;

    private float angleInc;

    private FlowParticleEmitter particleEmitter;

    private float lightAngle;

    private Text fps;

    private boolean once = false;

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
        System.out.println("Started Loading");
        long time = System.currentTimeMillis();
        angleInc = 0.05f;
        lightAngle = 45;

        this.test = test;
        setCurserStatus(false);
        gameHandler.getCamera().setPosition(0, 3, 0);
        var resourceManager = gameHandler.getResourceManager();
        Mesh[] mainPlayer = StaticModelLoader.load(resourceManager.getResource("player/steve.obj"), "/player",this,resourceManager);
        MeshGameItem object = new MeshGameItem(mainPlayer);
        object.setPosition(4, 3f, 4);
        object.setScale(0.3f);
//        object.setCollider(new BoxCollider(new Vector3(0, 0, 0), new Vector3(1, 1.5f, 1)));
//        object.getCollider().setUseGravity(true).setTrigger(false);
//        ((BoxCollider) object.getCollider()).setOffset(new Vector3(0, 0.7f, 0));

        add(object);
        player = object;
        //Load Blocks

        InstancedMesh mesh = new InstancedMesh(CubeData.vertex, CubeData.texture, CubeData.normal, CubeData.indices, 10000);
        InputStream io = Texture.class.getResourceAsStream("/example_texture.png");
        Texture grass = Utils.inputStreamToTexture(io);
        Material mt = new Material(grass);

        mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));
        mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/ovly2.png")));

        mt.setReflectance(0.3f);

        mesh.setMaterial(mt);
        MeshGameItem gi = new MeshGameItem(mesh);
        gi.setCollider(new ObjectBoxCollider(true, false));
        add(gi);
        gi.setPosition(5, 2, 5);
        collider = gi;
//        Texture skyb = Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/skybox.png"));
//        SkyBox skyBox = new SkyBox(skyb, true);
//        setSkyBox(skyBox);

        /*

        ==================================================
                       Test of Render Chunks
        ===================================================

         */
        RenderTexture txt1 = new RenderTexture(resourceManager.getResource("/example_texture.png"));
        RenderTexture txt2 = new RenderTexture(resourceManager.getResource("/oop.png"));
        System.out.println(resourceManager.getResource("/m.png").getInputStream());
        RenderTexture txt3 = new RenderTexture(resourceManager.getResource("/m.png"));
        TextureAtlas atlas = new TextureAtlas(Arrays.asList(txt1, txt2, txt3), Paths.get("").toAbsolutePath().toString(), this);
        setTextureAtlas(atlas);

        System.out.println(txt3.getYOffset());

//        for(int cx = 0; cx < 1; cx++){
//            for(int cz = 0; cz < 1; cz++){
//                RenderChunk rc = new RenderChunk(new ArrayList<>(), getTextureAtlas());
//                rc.setPosition(cx * 16, -16, cz * 16);
//                for(int x = 0; x < 16; x++){
//                    for(int y = 0; y < 16; y++){
//                        for(int z = 0; z < 16; z++){
//                            if(y > 6 && y < 10) continue;
//                            RenderBlock rb = new RenderBlock(new BlockLayout(), getTextureAtlas().getTextures().get(ThreadLocalRandom.current().nextInt(0, 3)), new Vector3(x, y, z));
//                            rc.addBlock(rb);
//                        }
//                    }
//                }
//                rc.regenerateChunkAsync(getTextureAtlas());
//                getChunkHandler().addChunk(rc);
//            }
//        }




        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int cx = 0; cx < 10; cx++){
                    for(int cy = 0; cy < 10; cy++){
                        for(int cz = 0; cz < 10; cz++){
                            RenderChunk rc = new RenderChunk(new ArrayList<>(), getTextureAtlas());
                            rc.setPosition(cx * 16, cy*16, cz * 16);
                            for(int x = 0; x < 16; x++){
                                for(int y = 0; y < 16; y++){
                                    for(int z = 0; z < 16; z++){
                                        RenderBlock rb = new RenderBlock(new BlockLayout(), getTextureAtlas().getTextures().get(ThreadLocalRandom.current().nextInt(0, 3)), new Vector3(x, y, z));
                                        rc.addBlock(rb);
                                    }
                                }
                            }

                            rc.regenerateChunkAsync(getTextureAtlas());

                            getChunkHandler().addChunk(rc);
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();



//        System.out.println(getChunkHandler().getRenderChunkList());


        MeshGameItem sh = (MeshGameItem) gi.clone(false);
        sh.setPosition(-4, 3, -4);
        this.add(sh);


        PointLight pointLight = new PointLight(new LightColor(255, 255, 0), new Vector3(1, 1, 1), 1);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        this.add(pointLight);


        DirectionalLight directionalLight = new DirectionalLight(new LightColor(255, 255, 255), new Vector3(0, 1, 0.5f), 0.5f);
        directionalLight.setShadowPosMult(8);
        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        getLightHandler().setDirectionalLight(directionalLight);

        /*

            The brand new canvas code.

         */

        setFog(new Fog(true, new Vector3(1f, 1f, 1f), 0.005f));


        ComponentCanvas cc = new ComponentCanvas(this);

        Font font = new Font("Roboto-Regular", resourceManager.getResource("Roboto-Regular.ttf"), this);
        hud.addFont(font);

        Text fps = new Text("FPS: 000", font);
        fps.setColor(new RGBA(255,255,255,1));

        fps.setPosition(20, 20);
        cc.add(fps);
        this.fps = fps;
        Rectangle rect = new Rectangle();
        rect.setColor(new RGBA(0, 255, 0, 1));
        rect.setScale(5, 5);
        rect.setPosition((float)gameHandler.getWindow().getWidth()/2, (float)gameHandler.getWindow().getHeight()/2);
        cc.add(rect);

        add(cc);

        /**
         * Particles
         */

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
        particle.setScale(scale);
        particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
        particleEmitter.setActive(true);
        particleEmitter.setPositionRndRange(range);
        particleEmitter.setSpeedRndRange(range);
        particleEmitter.setAnimRange(10);
        this.add(particleEmitter);


        this.handler = gameHandler;

        System.out.println("Done. Scene loaded in " + (time - System.currentTimeMillis()) + " ms");
    }

    @Override
    public void update(float interval) {
        KeyInput ki = handler.getKeyInput();

        if(!once){
            gameHandler.getEventManager().registerHandler(this, this);
            once = true;
        }

        fps.setText("FPS: " + Math.round(1/ Time.deltaTime));

        if (ki.isKeyPressed(GLFW_KEY_W)) {
            handler.getCamera().movePosition(0, 0, -1);
        }
        if (ki.isKeyPressed(GLFW_KEY_S)) {
            handler.getCamera().movePosition(0, 0, 1);
        }
        if (ki.isKeyPressed(GLFW_KEY_A)) {
            handler.getCamera().movePosition(-1, 0, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_D)) {
            handler.getCamera().movePosition(1, 0, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_SPACE)) {
            handler.getCamera().movePosition(0, 1, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            handler.getCamera().movePosition(0, -1, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_ESCAPE)) {
            test.exit();
        }
        if (ki.isKeyPressed(GLFW_KEY_TAB)) {
            this.setCurserStatus(!this.getCurserStatus());
        }

        Vector3 currentPos = collider.getPosition();
        if (ki.isKeyPressed(GLFW_KEY_UP)) {
            collider.translateBy(0.1f, 0, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_DOWN)) {
            collider.setPosition(currentPos.x - 0.1f, currentPos.y, currentPos.z);
        }
        if (ki.isKeyPressed(GLFW_KEY_LEFT)) {
            collider.setPosition(currentPos.x, currentPos.y, currentPos.z + 0.1f);
        }
        if (ki.isKeyPressed(GLFW_KEY_RIGHT)) {
            collider.setPosition(currentPos.x, currentPos.y, currentPos.z - 0.1f);
        }
        if(ki.isKeyPressed(GLFW_KEY_N)){
            collider.translateBy(0, 0.1f,0);
        }
        if(ki.isKeyPressed(GLFW_KEY_M)){
            collider.translateBy(0, -0.1f,0);
        }

//        if (ki.isKeyPressed(GLFW_KEY_I)) {
//            lightIndication.translateBy(0, 0, 1);
//        }
//        if (ki.isKeyPressed(GLFW_KEY_K)) {
//            lightIndication.translateBy(0, 0, -1);
//        }
//        if (ki.isKeyPressed(GLFW_KEY_J)) {
//            lightIndication.translateBy(-1, 0, 0);
//        }
//        if (ki.isKeyPressed(GLFW_KEY_L)) {
//            lightIndication.translateBy(1, 0, 0);
//        }

//        light.setPosition(lightIndication.getPosition());
//        getLightHandler().getSpotLight(0).setPosition(handler.getCamera().getPosition());

        MouseInput mi = handler.getMouseInput();
        handler.getCamera().moveRotation((float) (mi.getDeltaPosition().y), (float) mi.getDeltaPosition().x, 0);
        if (handler.getSoundManager().getListener() != null)
            handler.getSoundManager().getListener().setPosition(gameHandler.getCamera().getPosition());


        lightAngle += Time.deltaTime * 1.3;
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
    public void OnMouseClick(MouseClickEvent evt){
        if(evt.getMouseClickType() == MouseClickType.LEFT_CLICK){
            System.out.println("Clicked!");
            Collidable selected = this.selectGameItems(20);
            if(selected instanceof RenderBlock){
                RenderBlock block = (RenderBlock) selected;
                RenderChunk parentChunk = block.getParentChunk();
                parentChunk.removeBlock(block);
                parentChunk.regenerateChunkAsync(getTextureAtlas());
            }
        }
    }


}
