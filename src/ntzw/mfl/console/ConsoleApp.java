package ntzw.mfl.console;

import ntzw.mfl.AccountManager;
import ntzw.mfl.Main;
import ntzw.mfl.MultiFruitLaunch;
import ntzw.mfl.PasswordSupplier;
import ntzw.mfl.Settings;
import ntzw.mfl.auth.AuthStatus;
import ntzw.mfl.auth.GameAuthData;
import ntzw.mfl.auth.yggdrasil.YggdrasilAuthAgent;
import ntzw.mfl.json.JsonParser;
import ntzw.mfl.launch.Launcher;
import ntzw.mfl.launch.ProcessMonitor;
import ntzw.mfl.util.ArrayUtil;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Paths;

public class ConsoleApp {

    public void run(Settings settings, JsonParser jsonParser, String[] args) {
        boolean useSelected = ArrayUtil.contains(args, "--useSelected");
        if (!useSelected) {
            if (args.length < 4)
                invalidArgs();
            if (!ArrayUtil.contains(args, "--username") || !ArrayUtil.contains(args, "--authServer"))
                invalidArgs();
        }
        boolean captureGameIO = ArrayUtil.contains(args, "--captureGameIO") | settings.getBoolean(Main.REDIRECT_OUTPUT_SETTING_NAME);
        boolean maxMemArg = ArrayUtil.contains(args, "--maxMemory");
        String maxMemory = settings.getString(Main.MAX_MEMORY_SETTING_NAME);
        int index;
        if (maxMemArg) {
            index = ArrayUtil.getIndex(args, "--maxMemory") + 1;
            if (index >= args.length) invalidArgs();
            maxMemory = args[index];
        }

        AccountManager accountManager = new AccountManager();
        accountManager.loadAccounts(Paths.get(settings.getString(Main.ACCOUNT_STORAGE_PATH_SETTING_NAME)), jsonParser, true);

        String username = null;
        String authServer = null;
        if (!useSelected) {
            index = ArrayUtil.getIndex(args, "--username") + 1;
            if (index >= args.length) invalidArgs();
            username = args[index];

            index = ArrayUtil.getIndex(args, "--authServer") + 1;
            if (index >= args.length) invalidArgs();
            authServer = args[index];

            accountManager.selectAccount(username, authServer);
        }

        PasswordSupplier passwordSupplier = new PasswordSupplier() {
            @Override
            public String getPassword() {
                Console console = System.console();
                if (console == null) {
                    System.err.println("Couldn't get Console instance");
                    System.exit(0);
                }
                char[] passwordArray = console.readPassword("Enter password: ");
                return new String(passwordArray);
            }

            @Override
            public boolean isSupplied() {
                return true;
            }
        };

        if (accountManager.getSelectedAccountIndex() == -1) {
            if (useSelected) {
                System.err.println("No selected account");
                return;
            } else {
                System.out.println("Given username/authServer pair not found");
                System.out.println("Creating new one");
                while (accountManager.addAccountByUserInput(username, authServer,
                        new YggdrasilAuthAgent("Minecraft", 1), passwordSupplier) != AuthStatus.OK) {
                    System.out.println("Invalid password, try again");
                }
                accountManager.selectAccount(username, authServer);
            }
        }

        if (accountManager.authWithSelectedAccount(passwordSupplier) != AuthStatus.OK) {
            System.err.println("Authentication failed");
            return;
        }
        accountManager.saveAccounts();

        GameAuthData gameAuthData = accountManager.getGameAuthData();
        try {
            Launcher launcher;
            if (maxMemArg) {
                launcher = new Launcher(MultiFruitLaunch.buildArguments(
                        gameAuthData.getPlayerName(),
                        gameAuthData.getPlayerId(),
                        gameAuthData.getAccessToken()
                ), "-Xmx" + maxMemory);
            } else {
                launcher = new Launcher(MultiFruitLaunch.buildArguments(
                        gameAuthData.getPlayerName(),
                        gameAuthData.getPlayerId(),
                        gameAuthData.getAccessToken()
                ));
            }
            ProcessMonitor processMonitor = new ProcessMonitor(launcher.runMinecraft(captureGameIO));
            if (captureGameIO) {
                System.out.println("Redirecting game IO enabled");
            } else {
                System.out.print("Will check game status in 10 seconds... ");
                if (processMonitor.wait(10)) {
                    System.out.println("OK!");
                } else {
                    System.err.println("Game process has been closed earlier! It's some kind of error, check game log file for details");
                    System.out.println();
                    System.out.println("Will close in 5 seconds...");
                    Thread.sleep(5000);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void invalidArgs() {
        System.err.println("Invalid input arguments");
        System.out.println("Required:");
        System.out.println("--username <>");
        System.out.println("--authServer <>");
        System.out.println("OR");
        System.out.println("--useSelected");
        System.out.println();
        System.out.println("Optional:");
        System.out.println("--maxMemory <>");
        System.out.println("--captureGameIO");
        System.exit(1);
    }
}
