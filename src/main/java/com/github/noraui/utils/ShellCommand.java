/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.noraui.exception.TechnicalException;

public class ShellCommand {

    /**
     * Specific logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ShellCommand.class);

    private static final String SHELL_RUNNING_COMMAND = "SHELL_RUNNING_COMMAND";
    private final String command;
    private final String[] parameters;

    public ShellCommand(String command, String... parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public int run() throws TechnicalException {
        final Runtime rt = Runtime.getRuntime();
        final List<String> cmdList = new ArrayList<>();
        cmdList.add(command);
        logger.info(Messages.getMessage(SHELL_RUNNING_COMMAND), command);
        for (final String param : parameters) {
            logger.info(param);
            cmdList.add(param);
        }
        try {
            final Process p = rt.exec(cmdList.toArray(new String[cmdList.size()]));
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            return p.waitFor();
        } catch (IOException | InterruptedException e) {
            logger.error("error ShellCommand.run()", e);
            Thread.currentThread().interrupt();
            throw new TechnicalException(e.getMessage(), e);
        }
    }
}
