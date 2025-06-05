package com.example.todo;


import io.helidon.microprofile.server.Server;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;


/**
 * タスク管理アプリのメインクラス
 * <p>
 * Helidon MicroProfile サーバを起動し、アプリケーションを開始する
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String... args) {
        if (System.getProperty("java.util.logging.manager") == null) {
            System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        }
        logger.info("ToDoアプリケーションを開始します...");

        try {
            // Helidon MicroProfile サーバを起動
            Server server = startServer();

            // シャットダウンフックを登録
            registerShutdownHook(server);

            logger.info("アプリケーションが正常に起動しました");

            printApplicationInfo(server);
        } catch (Exception e) {
            logger.error("アプリケーションの起動に失敗しました", e);
            System.exit(1);
        }
    }

    /**
     * アプリケーション情報を表示
     *
     * @param server 起動済サーバー
     */
    private static void printApplicationInfo(Server server) {
        String host = server.host();
        int port = server.port();

        logger.info("========================================");
        logger.info("  ToDoアプリケーション起動完了");
        logger.info("========================================");
        logger.info("  Application URL    : http://{}:{}/", host, port);
        logger.info("  API Base URL       : http://{}:{}/api/", host, port);
        logger.info("  Health check       : http://{}:{}/health", host, port);
        logger.info("  Metrics            : http://{}:{}/metrics", host, port);
        logger.info("  OpenAPI            : http://{}:{}/openapi", host, port);
        logger.info("========================================");

        // 開発時の便利情報
        if (isDevelopmentMode()) {
            logger.info("  開発モードで実行中");
            logger.info("  H2 Database Console: http://{}:{}/h2-console", host, port);
            logger.info("  Profile            : development");
        }

        logger.info("アプリケーションを停止するには Ctrl+C を押してください");
    }

    /**
     * Helidon MicroProfile サーバーを起動
     *
     * @return 起動したサーバーインスタンス
     */
    private static Server startServer() {
        long startTime = System.currentTimeMillis();

        // サーバーを作成・起動
        Server server = Server.create().start();

        long duration = System.currentTimeMillis() - startTime;
        logger.info("サーバーが起動しました　（起動時間：{}ms）", duration);

        return server;
    }

    /**
     * シャットダウンフックを登録
     * <p>
     * アプリケーション終了時の適切なクリーンアップを実行
     *
     * @param server 管理対象サーバー
     */
    private static void registerShutdownHook(Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("アプリケーションを停止しています...");

            try {
                // CDIコンテナを停止
                SeContainer container = (SeContainer) CDI.current();
                if (container != null) {
                    container.close();
                    logger.info("CDI コンテナを停止しました");
                }

                // サーバを停止
                server.stop();
                logger.info("サーバーを停止しました");

                // 少し待機してリソースのクリーンアップを完了
                TimeUnit.MILLISECONDS.sleep(100);

                logger.info("アプリケーションが正常に停止しました");
            } catch (Exception e) {
                logger.error("アプリケーション停止中にエラーが発生しました", e);
            }
        }));
    }

    /**
     * 開発者モードかどうかを判定
     *
     * @return 開発モードの場合 {@code true}
     */
    private static boolean isDevelopmentMode() {
        String profile = System.getProperty("mp.config.profile", "development");
        return "development".equals(profile) || "dev".equals(profile);
    }

    /**
     * アプリケーションの正常性をチェック
     * <p>
     * 起動時やヘルスチェックで使用
     *
     * @return アプリケーションが正常な場合 {@code true}
     */
    public static boolean isHealthy() {
        try {
            // CDI コンテナが正常に動作しているかチェック
            SeContainer container = (SeContainer) CDI.current();
            if (container == null || !container.isRunning()) {
                return false;
            }

            // 必要に応じて追加のヘルスチェックを実装
            // 例：データベース接続確認、外部サービス接続確認など

            return true;
        } catch (Exception e) {
            logger.error("ヘルスチェックでエラーが発生しました", e);
            return false;
        }
    }

    /**
     * アプリケーションバージョン情報を取得
     *
     * @return バージョン文字列
     */
    public static String getVersion() {
        Package pkg = Main.class.getPackage();
        String version = pkg != null ? pkg.getImplementationVersion() : null;
        return version != null ? version : "1.0.0-SNAPSHOT";
    }

    /**
     * アプリケーション名を取得
     *
     * @return アプリケーション名
     */
    public static String getApplicationName() {
        return "Todo Application";
    }
}
