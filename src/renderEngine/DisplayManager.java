package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            /*
            DisplayMode[] modes = Display.getAvailableDisplayModes();

            for (int i=0;i<modes.length;i++) {
                DisplayMode current = modes[i];
                System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
                        current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
            }
            */

            /*
            -If the edges of the water quad look a bit jagged (especially when you zoom out with the camera)
            then you may have a problem with the precision of the depth buffer. This can be fixed by increasing
            the NEAR_PLANE value of your projection matrix in the MasterRenderer, or by changing "new PixelFormat()"
             to "new PixelFormat().withDepthBits(24)" when creating the display.
             */



            DisplayMode mode = new DisplayMode(WIDTH, HEIGHT);
            //DisplayMode mode = Display.getDesktopDisplayMode();
            Display.setDisplayMode(mode);
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("OpenGL 3D Game Tutorial 28: Day/Night");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    // returns current time in milliseconds
    private static long getCurrentTime() {
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }
}
