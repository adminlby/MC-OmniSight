package org.lbynb.mCOmniSight.api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.lbynb.mCOmniSight.camera.CameraNode;
import org.lbynb.mCOmniSight.database.DatabaseManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class MASSApiServer {

    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/cameras", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    List<CameraNode> activeCameras = DatabaseManager.loadAllCameras().stream()
                            .filter(CameraNode::isActive)
                            .collect(Collectors.toList());

                    String jsonResponse = activeCameras.stream()
                            .map(camera -> String.format(
                                    "{\"id\":\"%s\",\"x\":%f,\"y\":%f,\"z\":%f,\"world\":\"%s\",\"type\":\"%s\",\"yaw\":%f,\"pitch\":%f}",
                                    camera.getId(),
                                    camera.getLoc().getX(),
                                    camera.getLoc().getY(),
                                    camera.getLoc().getZ(),
                                    camera.getLoc().getWorld().getName(),
                                    camera.getType(),
                                    camera.getYaw(),
                                    camera.getPitch()
                            ))
                            .collect(Collectors.joining(",", "[", "]"));

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            }
        });

        server.createContext("/api/player/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    String path = exchange.getRequestURI().getPath();
                    String playerName = path.substring(path.lastIndexOf("/") + 1);

                    Player player = Bukkit.getPlayer(playerName);
                    String jsonResponse;

                    if (player != null && player.isOnline()) {
                        jsonResponse = String.format(
                                "{\"name\":\"%s\",\"hp\":%f,\"food\":%d,\"helmet\":\"%s\",\"chestplate\":\"%s\",\"leggings\":\"%s\",\"boots\":\"%s\",\"x\":%f,\"y\":%f,\"z\":%f,\"world\":\"%s\",\"online\":true}",
                                player.getName(),
                                player.getHealth(),
                                player.getFoodLevel(),
                                player.getInventory().getHelmet() != null ? player.getInventory().getHelmet().getType().name() : "none",
                                player.getInventory().getChestplate() != null ? player.getInventory().getChestplate().getType().name() : "none",
                                player.getInventory().getLeggings() != null ? player.getInventory().getLeggings().getType().name() : "none",
                                player.getInventory().getBoots() != null ? player.getInventory().getBoots().getType().name() : "none",
                                player.getLocation().getX(),
                                player.getLocation().getY(),
                                player.getLocation().getZ(),
                                player.getLocation().getWorld().getName()
                        );
                    } else {
                        jsonResponse = String.format("{\"name\":\"%s\",\"online\":false}", playerName);
                    }

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            }
        });

        server.setExecutor(null); // Use the default executor
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}
