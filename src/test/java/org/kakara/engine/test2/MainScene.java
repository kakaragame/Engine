package org.kakara.engine.test2;

import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.KeyPressEvent;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.input.Input;
import org.kakara.engine.input.controller.ids.ControllerID;
import org.kakara.engine.input.controller.ids.GamePadAxis;
import org.kakara.engine.input.controller.ids.GamePadButton;
import org.kakara.engine.input.key.KeyCode;
import org.kakara.engine.input.mouse.MouseInput;
import org.kakara.engine.lighting.LightColor;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.lighting.SpotLight;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.models.StaticModelLoader;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.AbstractGameScene;
import org.kakara.engine.utils.Time;

public class MainScene extends AbstractGameScene {
    public MainScene(GameHandler gameHandler) {
        super(gameHandler);
    }

    @Override
    public void work() {

    }

    @Override
    public void loadGraphics(GameHandler gameHandler) throws Exception {
        ResourceManager rm = gameHandler.getResourceManager();

        Mesh[] monkeyMesh = StaticModelLoader.load(rm.getResource("/test2/Monkey.obj"), "/test2", this, rm);
        GameItem monkey = new GameItem(monkeyMesh);
        monkey.transform.setPosition(0, 0, -10);
        add(monkey);

        // Show a spotlight.
        SpotLight spotLight = new SpotLight(new Vector3(0, 0, 30), new Vector3f(0, 0, -1), (float) Math.cos(Math.toRadians(140)));
        spotLight.setIntensity(10);
        spotLight.setColor(LightColor.PURPLE);
        add(spotLight);
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        System.out.println(event.getKeyName());
    }

    @Override
    public void update(float v) {
        // Controller
        if (getControllerManager().controllerExists(ControllerID.CONTROLLER_ONE)) {
            float moveZ = Time.getDeltaTime() * 15f * Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.LEFT_STICK_Y);
            float moveX = Time.getDeltaTime() * 15f * Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.LEFT_STICK_X);
            // Move the position in the Z direction using the Left Stick
            getCamera().movePosition(0, 0, moveZ);
            // Move the position in the X direction using the Left Stick
            getCamera().movePosition(moveX, 0, 0);
            // Rotate the camera based upon the Right Stick.
            getCamera().moveRotation(Time.getDeltaTime() * 150f * Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.RIGHT_STICK_Y),
                    Time.getDeltaTime() * 150f * Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.RIGHT_STICK_X), 0);

            // Move the Camera up in the Y Direction.
            if (Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.A)) {
                getCamera().movePosition(0, 15f * Time.getDeltaTime(), 0);
            }
            // Move the Camera down in the Y Direction.
            if (Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.B)) {
                getCamera().movePosition(0, -15f * Time.getDeltaTime(), 0);
            }

            // Exit the game.
            if (Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.START))
                gameHandler.exit();
        }

        if (Input.isKeyDown(KeyCode.W)) {
            getCamera().movePosition(0, 0, Time.getDeltaTime() * -15f);
        }
        if (Input.isKeyDown(KeyCode.S)) {
            getCamera().movePosition(0, 0, Time.getDeltaTime() * 15f);
        }
        if (Input.isKeyDown(KeyCode.A)) {
            getCamera().movePosition(Time.getDeltaTime() * -15f, 0, 0);
        }
        if (Input.isKeyDown(KeyCode.D)) {
            getCamera().movePosition(Time.getDeltaTime() * 15f, 0, 0);
        }
        if (Input.isKeyDown(KeyCode.ESCAPE)) {
            gameHandler.exit();
        }

        setCursorStatus(false);

        MouseInput mouseInput = gameHandler.getMouseInput();
        getCamera().moveRotation((float) (Time.getDeltaTime() * 20f * (mouseInput.getDeltaPosition().y)), (float) (Time.getDeltaTime() * 20f * mouseInput.getDeltaPosition().x), 0);
    }
}