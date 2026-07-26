package com.rpgwave.world;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;

public class TmxLoader {

    public static TileMap load(String tmxPath, String mapsFolder) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputStream tmxStream = TmxLoader.class.getResourceAsStream(tmxPath);
            Document doc = builder.parse(tmxStream);
            doc.getDocumentElement().normalize();

            Element mapEl = doc.getDocumentElement();
            int mapWidth = Integer.parseInt(mapEl.getAttribute("width"));
            int mapHeight = Integer.parseInt(mapEl.getAttribute("height"));
            int tileWidth = Integer.parseInt(mapEl.getAttribute("tilewidth"));
            int tileHeight = Integer.parseInt(mapEl.getAttribute("tileheight"));

            List<Tileset> tilesets = new ArrayList<>();
            NodeList tilesetNodes = mapEl.getElementsByTagName("tileset");
            for (int i = 0; i < tilesetNodes.getLength(); i++) {
                Element tsEl = (Element) tilesetNodes.item(i);
                int firstGid = Integer.parseInt(tsEl.getAttribute("firstgid"));
                String source = tsEl.getAttribute("source");
                String tsxFileName = source.substring(source.lastIndexOf('/') + 1);

                try {
                    tilesets.add(loadTsx(mapsFolder + tsxFileName, mapsFolder, firstGid));
                } catch (Exception e) {
                    System.out.println("Aviso: tileset '" + tsxFileName + "' não carregou (" + e.getMessage() + ")");
                }
            }

            Map<String, int[][]> layers = new LinkedHashMap<>();
            NodeList layerNodes = mapEl.getElementsByTagName("layer");
            for (int i = 0; i < layerNodes.getLength(); i++) {
                Element layerEl = (Element) layerNodes.item(i);
                String name = layerEl.getAttribute("name");
                Element dataEl = (Element) layerEl.getElementsByTagName("data").item(0);
                String csv = dataEl.getTextContent();

                List<Integer> values = new ArrayList<>();
                for (String v : csv.split(",")) {
                    v = v.trim();
                    if (!v.isEmpty()) values.add(Integer.parseInt(v));
                }

                int[][] grid = new int[mapHeight][mapWidth];
                int idx = 0;
                for (int row = 0; row < mapHeight; row++) {
                    for (int col = 0; col < mapWidth; col++) {
                        grid[row][col] = values.get(idx++);
                    }
                }
                layers.put(name, grid);
            }

            return new TileMap(mapWidth, mapHeight, tileWidth, tileHeight, layers, tilesets);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar mapa: " + tmxPath, e);
        }
    }

    private static Tileset loadTsx(String tsxPath, String mapsFolder, int firstGid) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputStream tsxStream = TmxLoader.class.getResourceAsStream(tsxPath);
        Document doc = builder.parse(tsxStream);

        Element tilesetEl = doc.getDocumentElement();
        int tileWidth = Integer.parseInt(tilesetEl.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(tilesetEl.getAttribute("tileheight"));
        int tileCount = Integer.parseInt(tilesetEl.getAttribute("tilecount"));
        int columns = Integer.parseInt(tilesetEl.getAttribute("columns"));

        Element imageEl = (Element) tilesetEl.getElementsByTagName("image").item(0);
        String imageSource = imageEl.getAttribute("source");
        String imageFileName = imageSource.substring(imageSource.lastIndexOf('/') + 1);

        BufferedImage image = ImageIO.read(TmxLoader.class.getResourceAsStream(mapsFolder + imageFileName));

        return new Tileset(firstGid, tileCount, columns, tileWidth, tileHeight, image);
    }
}