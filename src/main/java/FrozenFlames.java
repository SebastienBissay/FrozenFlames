import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class FrozenFlames extends PApplet {

    private float[][] grid;

    public static void main(String[] args) {
        PApplet.main(FrozenFlames.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        noFill();
        blendMode(BLEND_MODE);

        initGrid();
    }

    @Override
    public void draw() {
        stroke(random(LOW_COLOR.red(), HIGH_COLOR.red()),
                random(LOW_COLOR.green(), HIGH_COLOR.green()),
                random(LOW_COLOR.blue(), HIGH_COLOR.blue()),
                random(LOW_COLOR.alpha(), HIGH_COLOR.alpha()));

        PVector point = new PVector(random(width), random(random(height), height));
        for (int k = 0; k < MAX_LENGTH; k++) {
            int i = floor(point.x / CELL_SIZE);
            int j = floor(point.y / CELL_SIZE);
            if (i < 0 || i >= GRID_SIZE || j < 0 || j >= GRID_SIZE) break;

            PVector direction = PVector.fromAngle(grid[i][j]).mult(VELOCITY);
            if (floor((point.x + direction.x) / CELL_SIZE) != i
                    || floor((point.y + direction.y) / CELL_SIZE) != j) {
                direction.mult(-1);
            }
            do {
                point.add(direction);
                point(point.x, point.y);
            } while (floor(point.x / CELL_SIZE) == i && floor(point.y / CELL_SIZE) == j);
        }

        if (frameCount >= NUMBER_OF_ITERATIONS) {
            noLoop();
            saveSketch(this);
        }
    }

    private void initGrid() {
        grid = new float[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int n = floor(map(j, 0, GRID_SIZE, 16, 4));
                grid[i][j] = floor(random(n)) * PI / n;
            }
        }

        float[][] newGrid = new float[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                float div = 0;
                float sum = 0;
                for (int k = max(i - 1, 0); k < min(GRID_SIZE, i + 1); k++) {
                    for (int l = max(j - 1, 0); l < min(GRID_SIZE, j + 1); l++) {
                        div += 1 / (.5f * abs(i - k) + .5f * abs(j - l) + 1);
                        sum += grid[k][l] / (.5f * abs(i - k) + .5f * abs(j - l) + 1);
                    }
                }
                newGrid[i][j] = (sum / div) % PI;
            }
        }
        grid = newGrid;
    }
}
