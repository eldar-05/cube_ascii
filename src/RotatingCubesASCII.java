import java.util.Arrays;

public class RotatingCubesASCII {
    static float A, B, C;
    static float cubeWidth;
    static int width = 80, height = 22;
    static float[] zBuffer = new float[width * height];
    static char[] buffer = new char[width * height];
    static char backgroundASCIICode = ' ';
    static int distanceFromCam = 200;
    static float horizontalOffset;
    static float K1 = 40;
    static float incrementSpeed = 0.6f;
    public static void main(String[] args) throws InterruptedException {
        System.out.print("\033[2J");
        while (true) {
            Arrays.fill(buffer, backgroundASCIICode);
            Arrays.fill(zBuffer, 0);
            cubeWidth = 20;
            drawCube();
            System.out.print("\033[H");
            for (int i = 0; i < width * height; i++) {
                System.out.print(i % width != 0 ? buffer[i] : "\n");
            }
            A += 0.05;
            B += 0.05;
            C += 0.01;
            Thread.sleep(16); //Frames per sec ~60fps
        }
    }

    static void drawCube() {
        for (float cubeX = -cubeWidth; cubeX < cubeWidth; cubeX += incrementSpeed) {
            for (float cubeY = -cubeWidth; cubeY < cubeWidth; cubeY += incrementSpeed) {
                calculateForSurface(cubeX, cubeY, -cubeWidth, '@');
                calculateForSurface(cubeWidth, cubeY, cubeX, '$');
                calculateForSurface(-cubeWidth, cubeY, -cubeX, '~');
                calculateForSurface(-cubeX, cubeY, cubeWidth, '#');
                calculateForSurface(cubeX, -cubeWidth, -cubeY, ';');
                calculateForSurface(cubeX, cubeWidth, cubeY, '+');
            }
        }
    }

    static float calculateX(float i, float j, float k) {
        return j * (float)Math.sin(A) * (float)Math.sin(B) * (float)Math.cos(C) - k * (float)Math.cos(A) * (float)Math.sin(B) * (float)Math.cos(C) + j * (float)Math.cos(A) * (float)Math.sin(C) + k * (float)Math.sin(A) * (float)Math.sin(C) + i * (float)Math.cos(B) * (float)Math.cos(C);
    }

    static float calculateY(float i, float j, float k) {
        return j * (float)Math.cos(A) * (float)Math.cos(C) + k * (float)Math.sin(A) * (float)Math.cos(C) - j * (float)Math.sin(A) * (float)Math.sin(B) * (float)Math.sin(C) + k * (float)Math.cos(A) * (float)Math.sin(B) * (float)Math.sin(C) - i * (float)Math.cos(B) * (float)Math.sin(C);
    }

    static float calculateZ(float i, float j, float k) {
        return k * (float)Math.cos(A) * (float)Math.cos(B) - j * (float)Math.sin(A) * (float)Math.cos(B) + i * (float)Math.sin(B);
    }

    static void calculateForSurface(float cubeX, float cubeY, float cubeZ, char ch) {
        float x = calculateX(cubeX, cubeY, cubeZ);
        float y = calculateY(cubeX, cubeY, cubeZ);
        float z = calculateZ(cubeX, cubeY, cubeZ) + distanceFromCam;
        float ooz = 1 / z;
        int xp = (int)(width / 2 + horizontalOffset + K1 * ooz * x * 2);
        int yp = (int)(height / 2 + K1 * ooz * y);
        int idx = xp + yp * width;
        if (idx >= 0 && idx < width * height) {
            if (ooz > zBuffer[idx]) {
                zBuffer[idx] = ooz;
                buffer[idx] = ch;
            }
        }
    }
}
