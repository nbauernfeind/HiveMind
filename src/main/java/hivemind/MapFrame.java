package hivemind;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class MapFrame extends JFrame {
    final static int POINT_OFFSET_X = 10;
    final static int POINT_OFFSET_Y = 30;
    final static int POINTS_PER_TILE = 8;

    private final String mapFilename;
    private final MapFrameListener mapFrameListener;
    private final TileType[][] tiles; //row, col

    private final int rows;
    private final int cols;

    Location startTile = null;
    Location endTile = null;

    public MapFrame(String mapFilename, MapFrameListener listener) {
        this.mapFilename = mapFilename;
        this.mapFrameListener = listener;
        this.tiles = MapLoader.loadMap(mapFilename);

        rows = tiles.length;
        cols = tiles[0].length;

        int sizeX = 10 + POINT_OFFSET_X + cols * POINTS_PER_TILE;
        int sizeY = 10 + POINT_OFFSET_Y + rows * POINTS_PER_TILE;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(sizeX, sizeY);
        setVisible(true);
        createBufferStrategy(2);

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                Location d = new Location((double) e.getX(), (double) e.getY());
                System.out.println(d.row + ", " + d.col + " -> " + d.x + ", " + d.y);

                if (d.row >= 0 && d.row < rows && d.col >= 0 && d.col < cols) {
                    if (startTile == null) {
                        startTile = d;
                    } else {
                        if (d.equals(startTile) || d.equals(endTile)) {
                            startTile = null;
                            endTile = null;
                        } else {
                            endTile = d;
                        }
                    }

                    //reset
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            if (tiles[r][c].ordinal() > TileType.GROUND.ordinal()) {
                                tiles[r][c] = TileType.GROUND;
                            }
                        }
                    }

                    if (startTile != null && endTile != null) {
                        mapFrameListener.update(startTile, endTile, tiles);
                    }

                }

                repaint();
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(Color.lightGray);
        drawGrid(graphics);

        graphics.setColor(Color.gray);
        for (int r = 0; r < tiles.length; r++) {
            TileType[] tileType = tiles[r];
            for (int c = 0; c < tileType.length; c++) {
                if (tileType[c] == TileType.WATER) {
                    drawTile(graphics, r, c);
                }
            }
        }

        graphics.setColor(Color.yellow);
        for (int r = 0; r < tiles.length; r++) {
            TileType[] tileType = tiles[r];
            for (int c = 0; c < tileType.length; c++) {
                if (tileType[c] == TileType.EXPLORED) {
                    drawTile(graphics, r, c);
                }
            }
        }

        graphics.setColor(Color.green);
        for (int r = 0; r < tiles.length; r++) {
            TileType[] tileType = tiles[r];
            for (int c = 0; c < tileType.length; c++) {
                if (tileType[c] == TileType.PATH) {
                    drawTile(graphics, r, c);
                }
            }
        }

        if (startTile != null) {
            graphics.setColor(Color.blue);
            drawTile(graphics, startTile.row, startTile.col);
        }
        if (endTile != null) {
            graphics.setColor(Color.red);
            drawTile(graphics, endTile.row, endTile.col);
        }
    }

    private void drawGrid(Graphics2D graphics) {
        final int rows = tiles.length;
        final int cols = tiles[0].length;

        for (int i = 0; i <= cols; i++) {
            Location d1 = new Location(0, i);
            Location d2 = new Location(rows, i);
            graphics.draw(new Line2D.Double(d1.x, d1.y, d2.x, d2.y)); //horizontals
        }
        for (int i = 0; i <= rows; i++) {
            Location d1 = new Location(i, 0);
            Location d2 = new Location(i, cols);
            graphics.draw(new Line2D.Double(d1.x, d1.y, d2.x, d2.y)); //verticals
        }

    }

    private void drawTile(Graphics2D graphics, int r, int c) {
        Location d = new Location(r, c);
        graphics.fill(new Rectangle2D.Double(d.x + 1, d.y + 1, POINTS_PER_TILE, POINTS_PER_TILE));
    }

    public void setTile(int row, int col, TileType type) {
        tiles[row][col] = type;
    }

    public TileType getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public static interface MapFrameListener {
        void update(Location start, Location end, TileType[][] tiles);
    }
}
