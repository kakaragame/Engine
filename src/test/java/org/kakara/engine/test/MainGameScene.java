package org.kakara.engine.test;

import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.components.MeshRenderer;
import org.kakara.engine.components.Transform;
import org.kakara.engine.debug.DebugCanvas;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.gameitems.mesh.AtlasMesh;
import org.kakara.engine.gameitems.mesh.InstancedMesh;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.gameitems.particles.FlowParticleEmitter;
import org.kakara.engine.gameitems.particles.Particle;
import org.kakara.engine.input.KeyInput;
import org.kakara.engine.input.MouseClickType;
import org.kakara.engine.input.MouseInput;
import org.kakara.engine.lighting.DirectionalLight;
import org.kakara.engine.lighting.LightColor;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.models.StaticModelLoader;
import org.kakara.engine.physics.collision.PhysicsComponent;
import org.kakara.engine.physics.collision.BoxCollider;
import org.kakara.engine.physics.collision.ColliderComponent;
import org.kakara.engine.physics.collision.RenderBlockCollider;
import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.renderobjects.RenderTexture;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.mesh.MeshType;
import org.kakara.engine.renderobjects.renderlayouts.BlockLayout;
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

import static org.lwjgl.glfw.GLFW.*;

public class MainGameScene extends AbstractGameScene {
    private GameItem player;
    private GameHandler handler;
    private GameItem collider;
    private PhysicsComponent physComp;
    private KakaraTest test;
    private GameItem blockSelector;

    private boolean stopped = false;

    private float angleInc;

    private FlowParticleEmitter particleEmitter;

    private float lightAngle;

    private Text fps;

    private final boolean once = false;

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
        try{
            System.out.println("Started Loading");
            long time = System.currentTimeMillis();
            angleInc = 0.05f;
            lightAngle = 45;

            this.test = test;
            setCursorStatus(false);
            getCamera().setPosition(0, 3, 0);
            var resourceManager = gameHandler.getResourceManager();
            Mesh[] mainPlayer = StaticModelLoader.load(resourceManager.getResource("player/steve.obj"), "/player",this,resourceManager);
            GameItem object = new GameItem(mainPlayer);
            object.transform.setPosition(0, 20, 0);
            object.transform.setScale(0.3f);
            object.getMeshRenderer().get().getMesh().setWireframe(true);
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

            Mesh me = new Mesh(CubeData.vertex, CubeData.texture, CubeData.normal, CubeData.indices);
            me.setMaterial(mt);

            GameItem gi = new GameItem(mesh);
            gi.addComponent(BoxCollider.class);
//            gi.addComponent(Resolver.class);
            System.out.println("TEST:: " + gi.getComponent(ColliderComponent.class));
            System.out.println(Transform.class.isAssignableFrom(MeshRenderer.class));
//        gi.getMesh().setWireframe(true);
            add(gi);
            gi.transform.setPosition(3, 16*2 + 5, 3);

            PhysicsComponent physicsComponent = gi.addComponent(PhysicsComponent.class);
            // Add a component to handle player movement.
            gi.addComponent(PlayerMovement.class);
            physicsComponent.setVelocity(new Vector3(0, -9.18f, 0));
            this.physComp = physicsComponent;
            collider = gi;

            GameItem gi2 = new GameItem(mesh);
            add(gi2);
            gi2.transform.setPosition(6, 16*2, 6);
            BoxCollider bc = gi2.addComponent(BoxCollider.class);
            bc.setTrigger(true);

            gi2.setTag("Test");

            gi.getComponent(BoxCollider.class).addOnTriggerEnter((ColliderComponent other) -> {
                if(other instanceof RenderBlockCollider) return;
                if(other.getGameItem().getTag().equals("Test")){
                    remove(gi2);
                }
            });



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
            RenderTexture txt3 = new RenderTexture(resourceManager.getResource("/ExampleBlock.png"));
            RenderTexture txt5 = new RenderTexture(resourceManager.getResource("/ovly2.png"));
            RenderTexture txt6 = new RenderTexture(resourceManager.getResource("/fewio.png"));
            TextureAtlas atlas = new TextureAtlas(Arrays.asList(txt1, txt2, txt3, txt5, txt6), Paths.get("").toAbsolutePath().toString(), this);
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


            new Thread(() -> {
                for(int cx = 0; cx < 7; cx++){
                    for(int cy = 0; cy < 2; cy++){
                        for(int cz = 0; cz < 7; cz++){
                            RenderChunk rc = new RenderChunk(new ArrayList<>(), getTextureAtlas());
                            rc.transform.setPosition(cx * 16, cy*16, cz * 16);
                            for(int x = 0; x < 16; x++){
                                for(int y = 0; y < 16; y++){
                                    for(int z = 0; z < 16; z++){
                                        RenderBlock rb;
                                        if(x%3 == 0){
                                            rb = new RenderBlock(new BlockLayout(), getTextureAtlas().getTextures().get(4), new Vector3(x, y, z));
                                            rb.setOpaque(false);
                                        }else{
                                            rb = new RenderBlock(new BlockLayout(), getTextureAtlas().getTextures().get(ThreadLocalRandom.current().nextInt(0, 3)), new Vector3(x, y, z));
                                        }
                                        if(x % 2 == 0){
                                            rb.setOverlay(getTextureAtlas().getTextures().get(3));
                                        }

                                        rc.addBlock(rb);
                                    }
                                }
                            }

                            rc.regenerateChunk(getTextureAtlas(), MeshType.MULTITHREAD);

                            getChunkHandler().addChunk(rc);
                        }
                    }
                }
            }).start();



//        System.out.println(getChunkHandler().getRenderChunkList());


            GameItem sh = (GameItem) gi.clone(false);
            sh.transform.setPosition(-4, 3, -4);
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
            userInterface.addFont(font);

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

            ObjectCanvas oc = new ObjectCanvas(this);
            AtlasMesh m = new AtlasMesh(txt2, getTextureAtlas(), new BlockLayout(), CubeData.vertex, CubeData.normal, CubeData.indices);

            UIObject ui = new UIObject(m);
            ui.setPosition((float)200, (float)200);
            ui.setScale(100);
            ui.getRotation().rotateX((float) Math.toRadians(50));
            ui.getRotation().rotateY((float) Math.toRadians(40));
            oc.add(ui);
            add(oc);

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
            particle.transform.setScale(scale);
            particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
            particleEmitter.setActive(true);
            particleEmitter.setPositionRndRange(range);
            particleEmitter.setSpeedRndRange(range);
            particleEmitter.setAnimRange(10);
            this.add(particleEmitter);


            this.handler = gameHandler;

            add(new DebugCanvas());

            System.out.println("Done. Scene loaded in " + (time - System.currentTimeMillis()) + " ms");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void update(float interval) {
        KeyInput ki = handler.getKeyInput();

        fps.setText("FPS: " + Math.round(1/ Time.getDeltaTime()));

        if (ki.isKeyPressed(GLFW_KEY_W)) {
            getCamera().movePosition(0, 0, -1);
        }
        if (ki.isKeyPressed(GLFW_KEY_S)) {
            getCamera().movePosition(0, 0, 1);
        }
        if (ki.isKeyPressed(GLFW_KEY_A)) {
            getCamera().movePosition(-1, 0, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_D)) {
            getCamera().movePosition(1, 0, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_SPACE)) {
            getCamera().movePosition(0, 1, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            getCamera().movePosition(0, -1, 0);
        }
        if (ki.isKeyPressed(GLFW_KEY_ESCAPE)) {
            test.exit();
        }
        if (ki.isKeyPressed(GLFW_KEY_TAB)) {
            this.setCursorStatus(true);
            stopped = !stopped;
        }


        MouseInput mi = handler.getMouseInput();
        if(!stopped)
            getCamera().moveRotation((float) (mi.getDeltaPosition().y), (float) mi.getDeltaPosition().x, 0);
        if (handler.getSoundManager().getListener() != null)
            handler.getSoundManager().getListener().setPosition(getCamera().getPosition());


        lightAngle += Time.getDeltaTime() * 1.3;
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
            ColliderComponent selected = this.selectGameItems(20);
            System.out.println(selected);
            if(selected instanceof RenderBlockCollider){
                System.out.println("Clicked!");
                RenderBlock block = ((RenderBlockCollider) selected).getRenderBlock();
                RenderChunk parentChunk = block.getParentChunk();
                parentChunk.removeBlock(block);
                parentChunk.regenerateChunk(getTextureAtlas(), MeshType.SYNC);
            }
        }
    }


}
