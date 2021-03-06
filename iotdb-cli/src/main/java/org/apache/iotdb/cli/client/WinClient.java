/**
 * Copyright © 2019 Apache IoTDB(incubating) (dev@iotdb.apache.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.iotdb.cli.client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.iotdb.cli.exception.ArgsErrorException;
import org.apache.iotdb.jdbc.Config;
import org.apache.iotdb.jdbc.IoTDBConnection;

import java.io.Console;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class WinClient extends AbstractClient {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName(Config.JDBC_DRIVER_NAME);
        IoTDBConnection connection = null;
        Options options = createOptions();
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(MAX_HELP_CONSOLE_WIDTH);
        CommandLine commandLine = null;
        CommandLineParser parser = new DefaultParser();

        if (args == null || args.length == 0) {
            System.out.println("Require more params input, please check the following hint.");
            hf.printHelp(IOTDB_CLI_PREFIX, options, true);
            return;
        }

        init();

        args = removePasswordArgs(args);

        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption(HELP_ARGS)) {
                hf.printHelp(IOTDB_CLI_PREFIX, options, true);
                return;
            }
            if (commandLine.hasOption(ISO8601_ARGS)) {
                setTimeFormat("long");
            }
            if (commandLine.hasOption(MAX_PRINT_ROW_COUNT_ARGS)) {
                try {
                    maxPrintRowCount = Integer.valueOf(commandLine.getOptionValue(MAX_PRINT_ROW_COUNT_ARGS));
                    if (maxPrintRowCount < 0) {
                        maxPrintRowCount = Integer.MAX_VALUE;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(IOTDB_CLI_PREFIX + "> error format of max print row count, it should be number");
                    return;
                }
            }
        } catch (ParseException e) {
            System.out.println("Require more params input, please check the following hint.");
            hf.printHelp(IOTDB_CLI_PREFIX, options, true);
            return;
        }
        Scanner scanner = null;
        try {
            String s;

            try {
                host = checkRequiredArg(HOST_ARGS, HOST_NAME, commandLine, false, host);
                port = checkRequiredArg(PORT_ARGS, PORT_NAME, commandLine, false, port);
                username = checkRequiredArg(USERNAME_ARGS, USERNAME_NAME, commandLine, true, null);

                password = commandLine.getOptionValue(PASSWORD_ARGS);
                if (password == null) {
                    password = readPassword();
                }
                try {
                    connection = (IoTDBConnection) DriverManager
                            .getConnection(Config.IOTDB_URL_PREFIX + host + ":" + port + "/", username, password);
                    properties = connection.getServerProperties();
                    AGGREGRATE_TIME_LIST.addAll(properties.getSupportedTimeAggregationOperations());
                } catch (SQLException e) {
                    System.out.println(IOTDB_CLI_PREFIX + "> " + e.getMessage());
                    return;
                }
            } catch (ArgsErrorException e) {
                // System.out.println(TSFILEDB_CLI_PREFIX + ": " + e.getMessage());
                return;
            }

            displayLogo(properties.getVersion());

            System.out.println(IOTDB_CLI_PREFIX + "> login successfully");
            scanner = new Scanner(System.in);
            while (true) {
                System.out.print(IOTDB_CLI_PREFIX + "> ");
                s = scanner.nextLine();
                if (s == null) {
                    continue;
                } else {
                    String[] cmds = s.trim().split(";");
                    for (int i = 0; i < cmds.length; i++) {
                        String cmd = cmds[i];
                        if (cmd != null && !cmd.trim().equals("")) {
                            OPERATION_RESULT result = handleInputInputCmd(cmd, connection);
                            switch (result) {
                            case RETURN_OPER:
                                return;
                            case CONTINUE_OPER:
                                continue;
                            default:
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(IOTDB_CLI_PREFIX + "> exit client with error " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static String readPassword() {
        Console c = System.console();
        if (c == null) { // IN ECLIPSE IDE
            System.out.print(IOTDB_CLI_PREFIX + "> please input password: ");
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        } else { // Outside Eclipse IDE
            return new String(c.readPassword(IOTDB_CLI_PREFIX + "> please input password: "));
        }
    }
}
