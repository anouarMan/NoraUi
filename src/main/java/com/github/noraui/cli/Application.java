/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Application {

    /**
     * @return
     */
    public List<String> get() {
        List<String> applications = new ArrayList<>();
        String selectorsPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "selectors";
        String[] versions = new File(selectorsPath).list();
        for (String version : versions) {
            applications.addAll(Arrays.asList(new File(selectorsPath + File.separator + version).list()));
        }
        TreeSet<String> hs = new TreeSet<>();
        hs.addAll(applications);
        applications.clear();
        applications.addAll(hs);
        for (int i = 0; i < applications.size(); i++) {
            applications.set(i, applications.get(i).replaceAll(".ini", ""));
        }
        return applications;
    }

    /**
     * Add new target application to your robot.
     * Sample if you add google: -f 1 -a google -u http://www.google.com --verbose
     * 
     * @param applicationName
     * @param url
     * @param robotContext
     * @param verbose
     */
    public void add(String applicationName, String url, Class<?> robotContext, boolean verbose) {
        System.out.println("Add a new application named [" + applicationName + "] with this url: " + url);
        addApplicationPages(applicationName, robotContext.getSimpleName().replaceAll("Context", ""), robotContext, verbose);
        addApplicationSteps(applicationName, robotContext.getSimpleName().replaceAll("Context", ""), robotContext, verbose);
        addApplicationContext(robotContext, applicationName, verbose);
        addApplicationSelector(applicationName, verbose);
        addApplicationInPropertiesFile(applicationName, robotContext.getSimpleName().replaceAll("Context", ""), verbose);
        addApplicationInEnvPropertiesFile(applicationName, url, "ci", verbose);
        addApplicationInEnvPropertiesFile(applicationName, url, "dev", verbose);
        addApplicationInEnvPropertiesFile(applicationName, url, "prod", verbose);
    }

    /**
     * Remove target application to your robot.
     * Sample if you add google: -f 4 -a google -u http://www.google.com --verbose
     * 
     * @param applicationName
     * @param url
     * @param robotContext
     * @param verbose
     */
    public void remove(String applicationName, Class<?> robotContext, boolean verbose) {
        System.out.println("Remove application named [" + applicationName + "]");
        removeApplicationPages(applicationName, robotContext.getSimpleName().replaceAll("Context", ""), robotContext, verbose);
        removeApplicationSteps(applicationName, robotContext.getSimpleName().replaceAll("Context", ""), robotContext, verbose);
        removeApplicationContext(robotContext, applicationName, verbose);
        removeApplicationSelector(applicationName, verbose);
        removeApplicationInPropertiesFile(applicationName, robotContext.getSimpleName().replaceAll("Context", ""), verbose);
        removeApplicationInEnvPropertiesFile(applicationName, "ci", verbose);
        removeApplicationInEnvPropertiesFile(applicationName, "dev", verbose);
        removeApplicationInEnvPropertiesFile(applicationName, "prod", verbose);
    }

    /**
     * @param applicationName
     * @param noraRobotName
     * @param robotContext
     * @param verbose
     */
    private void addApplicationPages(String applicationName, String noraRobotName, Class<?> robotContext, boolean verbose) {
        String pagePath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/pages/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page")
                + ".java";
        StringBuilder sb = new StringBuilder();
        sb.append("/**").append(System.lineSeparator());
        sb.append(" * " + noraRobotName + " generated free by NoraUi Organization https://github.com/NoraUi").append(System.lineSeparator());
        sb.append(" * " + noraRobotName + " is licensed under the license BSD.").append(System.lineSeparator());
        sb.append(" * ").append(System.lineSeparator());
        sb.append(" * CAUTION: " + noraRobotName + " use NoraUi library. This project is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE").append(System.lineSeparator());
        sb.append(" */").append(System.lineSeparator());
        sb.append(robotContext.getPackage().toString().replaceAll("utils", "application.pages." + applicationName) + ";").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import static " + robotContext.getCanonicalName() + "." + applicationName.toUpperCase() + "_KEY;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import org.openqa.selenium.support.ui.ExpectedConditions;").append(System.lineSeparator());
        sb.append("import org.slf4j.Logger;").append(System.lineSeparator());
        sb.append("import org.slf4j.LoggerFactory;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import com.github.noraui.application.page.Page;").append(System.lineSeparator());
        sb.append("import com.github.noraui.utils.Context;").append(System.lineSeparator());
        sb.append("import " + robotContext.getCanonicalName() + ";").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("public class " + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page extends Page {").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * Specific logger").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    private static final Logger logger = LoggerFactory.getLogger(" + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page.class);")
                .append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public final PageElement pageElementSample = new PageElement(\"-pageElementSample\", \"PageElement Sample\");").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    private static final String TITLE_PAGE = \"" + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "\";").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public " + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page() {").append(System.lineSeparator());
        sb.append("        super();").append(System.lineSeparator());
        sb.append("        this.application = " + applicationName.toUpperCase() + "_KEY;").append(System.lineSeparator());
        sb.append("        this.pageKey = \"" + applicationName.toUpperCase() + "_HOM\";").append(System.lineSeparator());
        sb.append("        this.callBack = Context.getCallBack(" + noraRobotName + "Context.CLOSE_WINDOW_AND_SWITCH_TO_" + applicationName.toUpperCase() + "_HOME);").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public boolean checkPage(Object... elements) {").append(System.lineSeparator());
        sb.append("        try {").append(System.lineSeparator());
        sb.append("            Context.waitUntil(ExpectedConditions.not(ExpectedConditions.titleIs(\"\")));").append(System.lineSeparator());
        sb.append("            if (!TITLE_PAGE.equals(getDriver().getTitle())) {").append(System.lineSeparator());
        sb.append("                logger.error(\"HTML title is not good\");").append(System.lineSeparator());
        sb.append("                return false;").append(System.lineSeparator());
        sb.append("            }").append(System.lineSeparator());
        sb.append("            return true;").append(System.lineSeparator());
        sb.append("        } catch (Exception e) {").append(System.lineSeparator());
        sb.append("            logger.error(\"HTML title Exception\", e);").append(System.lineSeparator());
        sb.append("            return false;").append(System.lineSeparator());
        sb.append("        }").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        try {
            FileUtils.forceMkdir(new File(pagePath.substring(0, pagePath.lastIndexOf(File.separator))));
            File newSelector = new File(pagePath);
            if (!newSelector.exists()) {
                Files.asCharSink(newSelector, Charsets.UTF_8).write(sb.toString());
            }
        } catch (Exception e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param noraRobotName
     * @param robotContext
     * @param verbose
     */
    private void removeApplicationPages(String applicationName, String noraRobotName, Class<?> robotContext, boolean verbose) {
        String pagePath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/pages/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page")
                + ".java";
        try {
            FileUtils.forceDelete(new File(pagePath));
            if (verbose) {
                System.out.println(pagePath + " removed with success.");
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param noraRobotName
     * @param robotContext
     * @param verbose
     */
    private void addApplicationSteps(String applicationName, String noraRobotName, Class<?> robotContext, boolean verbose) {
        String stepsPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/steps/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Steps")
                + ".java";
        StringBuilder sb = new StringBuilder();
        sb.append("/**").append(System.lineSeparator());
        sb.append(" * " + noraRobotName + " generated free by NoraUi Organization https://github.com/NoraUi").append(System.lineSeparator());
        sb.append(" * " + noraRobotName + " is licensed under the license BSD.").append(System.lineSeparator());
        sb.append(" * ").append(System.lineSeparator());
        sb.append(" * CAUTION: " + noraRobotName + " use NoraUi library. This project is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE").append(System.lineSeparator());
        sb.append(" */").append(System.lineSeparator());
        sb.append(robotContext.getPackage().toString().replaceAll("utils", "application.steps." + applicationName) + ";").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import com.github.noraui.application.steps.Step;").append(System.lineSeparator());
        sb.append("import com.github.noraui.exception.FailureException;").append(System.lineSeparator());
        sb.append("import com.github.noraui.exception.Result;").append(System.lineSeparator());
        sb.append("import com.github.noraui.utils.Messages;").append(System.lineSeparator());
        sb.append("import com.google.inject.Inject;").append(System.lineSeparator());
        sb.append("import " + robotContext.getCanonicalName().replaceAll("utils", "application.pages." + applicationName).replaceAll(robotContext.getSimpleName(),
                applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page;")).append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import cucumber.api.java.en.Then;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("public class " + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Steps extends Step {").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    @Inject").append(System.lineSeparator());
        sb.append("    private " + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Page " + applicationName + "Page;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * Check Login page.").append(System.lineSeparator());
        sb.append("     *").append(System.lineSeparator());
        sb.append("     * @throws FailureException").append(System.lineSeparator());
        sb.append("     *             if the scenario encounters a functional error.").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Then(\"The " + applicationName.toUpperCase() + " home page is displayed\")").append(System.lineSeparator());
        sb.append("    public void check" + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "HomePage() throws FailureException {").append(System.lineSeparator());
        sb.append("        if (!" + applicationName + "Page.checkPage()) {").append(System.lineSeparator());
        sb.append("            new Result.Failure<>(" + applicationName + "Page.getApplication(), Messages.getMessage(Messages.FAIL_MESSAGE_UNKNOWN_CREDENTIALS), true, " + applicationName
                + "Page.getCallBack());").append(System.lineSeparator());
        sb.append("        }").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        try {
            FileUtils.forceMkdir(new File(stepsPath.substring(0, stepsPath.lastIndexOf(File.separator))));
            File newSelector = new File(stepsPath);
            if (!newSelector.exists()) {
                Files.asCharSink(newSelector, Charsets.UTF_8).write(sb.toString());
            }
        } catch (Exception e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param noraRobotName
     * @param robotContext
     * @param verbose
     */
    private void removeApplicationSteps(String applicationName, String noraRobotName, Class<?> robotContext, boolean verbose) {
        String stepsPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/steps/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Steps")
                + ".java";
        try {
            FileUtils.forceDelete(new File(stepsPath));
            if (verbose) {
                System.out.println(stepsPath + " removed with success.");
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param robotContext
     * @param applicationName
     * @param verbose
     */
    private void addApplicationContext(Class<?> robotContext, String applicationName, boolean verbose) {
        manageApplicationContext(true, robotContext, applicationName, verbose);
    }

    /**
     * @param robotContext
     * @param applicationName
     * @param verbose
     */
    private void removeApplicationContext(Class<?> robotContext, String applicationName, boolean verbose) {
        manageApplicationContext(false, robotContext, applicationName, verbose);
    }

    /**
     * @param addMode
     * @param robotContext
     * @param applicationName
     * @param verbose
     */
    private void manageApplicationContext(boolean addMode, Class<?> robotContext, String applicationName, boolean verbose) {
        String contextPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("/", Matcher.quoteReplacement(File.separator)) + ".java";
        if (verbose) {
            System.out.println("Add application named [" + applicationName + "] in context.");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(contextPath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                if (!(("    public static final String " + applicationName.toUpperCase() + "_KEY = \"" + applicationName + "\";").equals(line)
                        || ("    public static final String " + applicationName.toUpperCase() + "_HOME = \"" + applicationName.toUpperCase() + "_HOME\";").equals(line)
                        || ("    private String " + applicationName + "Home; // " + applicationName.toUpperCase() + " home url").equals(line)
                        || ("    public static final String GO_TO_" + applicationName.toUpperCase() + "_HOME = \"GO_TO_" + applicationName.toUpperCase() + "_HOME\";").equals(line)
                        || ("    public static final String CLOSE_WINDOW_AND_SWITCH_TO_" + applicationName.toUpperCase() + "_HOME = \"CLOSE_WINDOW_AND_SWITCH_TO_" + applicationName.toUpperCase()
                                + "_HOME\";").equals(line)
                        || ("    public static final String CLOSE_ALL_WINDOWS_AND_SWITCH_TO_" + applicationName.toUpperCase() + "_HOME = \"CLOSE_ALL_WINDOWS_AND_SWITCH_TO_"
                                + applicationName.toUpperCase() + "_HOME\";").equals(line)
                        || ("        " + applicationName + "Home = getProperty(" + applicationName.toUpperCase() + "_KEY, applicationProperties);").equals(line)
                        || ("        initApplicationDom(clazz.getClassLoader(), selectorsVersion, " + applicationName.toUpperCase() + "_KEY);").equals(line)
                        || ("        exceptionCallbacks.put(GO_TO_" + applicationName.toUpperCase() + "_HOME, STEPS_BROWSER_STEPS_CLASS_QUALIFIED_NAME, GO_TO_URL_METHOD_NAME, "
                                + applicationName.toUpperCase() + "_HOME);").equals(line)
                        || ("        exceptionCallbacks.put(CLOSE_WINDOW_AND_SWITCH_TO_" + applicationName.toUpperCase()
                                + "_HOME, STEPS_BROWSER_STEPS_CLASS_QUALIFIED_NAME, \"closeWindowAndSwitchTo\", " + applicationName.toUpperCase() + "_KEY, " + applicationName.toUpperCase()
                                + "_HOME);").equals(line)
                        || ("        exceptionCallbacks.put(CLOSE_ALL_WINDOWS_AND_SWITCH_TO_" + applicationName.toUpperCase()
                                + "_HOME, STEPS_BROWSER_STEPS_CLASS_QUALIFIED_NAME, \"closeAllWindowsAndSwitchTo\", " + applicationName.toUpperCase() + "_KEY);").equals(line)
                        || ("        applications.put(" + applicationName.toUpperCase() + "_KEY, new Application(" + applicationName.toUpperCase() + "_HOME, " + applicationName + "Home));")
                                .equals(line))) {

                    if (("    public String get" + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Home() {").equals(line)) {
                        line = br.readLine();
                        line = br.readLine();
                        line = br.readLine();
                    }
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    if (addMode) {
                        if ("    // applications".equals(line)) {
                            sb.append("    public static final String " + applicationName.toUpperCase() + "_KEY = \"" + applicationName + "\";");
                            sb.append(System.lineSeparator());
                            sb.append("    public static final String " + applicationName.toUpperCase() + "_HOME = \"" + applicationName.toUpperCase() + "_HOME\";");
                            sb.append(System.lineSeparator());
                            sb.append("    private String " + applicationName + "Home; // " + applicationName.toUpperCase() + " home url");
                            sb.append(System.lineSeparator());
                        }
                        if ("    // targets".equals(line)) {
                            sb.append("    public static final String GO_TO_" + applicationName.toUpperCase() + "_HOME = \"GO_TO_" + applicationName.toUpperCase() + "_HOME\";");
                            sb.append(System.lineSeparator());
                            sb.append("    public static final String CLOSE_WINDOW_AND_SWITCH_TO_" + applicationName.toUpperCase() + "_HOME = \"CLOSE_WINDOW_AND_SWITCH_TO_"
                                    + applicationName.toUpperCase() + "_HOME\";");
                            sb.append(System.lineSeparator());
                            sb.append("    public static final String CLOSE_ALL_WINDOWS_AND_SWITCH_TO_" + applicationName.toUpperCase() + "_HOME = \"CLOSE_ALL_WINDOWS_AND_SWITCH_TO_"
                                    + applicationName.toUpperCase() + "_HOME\";");
                            sb.append(System.lineSeparator());
                        }
                        if ("        // Urls configuration".equals(line)) {
                            sb.append("        " + applicationName + "Home = getProperty(" + applicationName.toUpperCase() + "_KEY, applicationProperties);");
                            sb.append(System.lineSeparator());
                        }
                        if ("        // Selectors configuration".equals(line)) {
                            sb.append("        initApplicationDom(clazz.getClassLoader(), selectorsVersion, " + applicationName.toUpperCase() + "_KEY);");
                            sb.append(System.lineSeparator());
                        }
                        if ("        // Exception Callbacks".equals(line)) {
                            sb.append("        exceptionCallbacks.put(GO_TO_" + applicationName.toUpperCase() + "_HOME, STEPS_BROWSER_STEPS_CLASS_QUALIFIED_NAME, GO_TO_URL_METHOD_NAME, "
                                    + applicationName.toUpperCase() + "_HOME);");
                            sb.append(System.lineSeparator());
                            sb.append("        exceptionCallbacks.put(CLOSE_WINDOW_AND_SWITCH_TO_" + applicationName.toUpperCase()
                                    + "_HOME, STEPS_BROWSER_STEPS_CLASS_QUALIFIED_NAME, \"closeWindowAndSwitchTo\", " + applicationName.toUpperCase() + "_KEY, " + applicationName.toUpperCase()
                                    + "_HOME);");
                            sb.append(System.lineSeparator());
                            sb.append("        exceptionCallbacks.put(CLOSE_ALL_WINDOWS_AND_SWITCH_TO_" + applicationName.toUpperCase()
                                    + "_HOME, STEPS_BROWSER_STEPS_CLASS_QUALIFIED_NAME, \"closeAllWindowsAndSwitchTo\", " + applicationName.toUpperCase() + "_KEY);");
                            sb.append(System.lineSeparator());
                        }
                        if ("        // applications mapping".equals(line)) {
                            sb.append("        applications.put(" + applicationName.toUpperCase() + "_KEY, new Application(" + applicationName.toUpperCase() + "_HOME, " + applicationName + "Home));");
                            sb.append(System.lineSeparator());
                        }
                        if ("    // home getters".equals(line)) {
                            sb.append("    public String get" + applicationName.toUpperCase().charAt(0) + applicationName.substring(1) + "Home() {");
                            sb.append(System.lineSeparator());
                            sb.append("        return " + applicationName + "Home;");
                            sb.append(System.lineSeparator());
                            sb.append("    }");
                            sb.append(System.lineSeparator());
                        }
                    }
                }
                line = br.readLine();
            }
            FileWriter fw = new FileWriter(contextPath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString().substring(0, sb.toString().length() - System.lineSeparator().length()));
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param verbose
     */
    private void addApplicationSelector(String applicationName, boolean verbose) {
        String selectorsPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "selectors";
        String[] versions = new File(selectorsPath).list();
        StringBuilder sb = new StringBuilder();
        sb.append("[" + applicationName.toUpperCase() + "_HOM-pageElementSample]");
        sb.append(System.lineSeparator());
        sb.append("xpath=//*[@id='page-element-sample']");
        try {
            for (String version : versions) {
                File newSelector = new File(selectorsPath + File.separator + version + File.separator + applicationName + ".ini");
                if (!newSelector.exists()) {
                    Files.asCharSink(newSelector, Charsets.UTF_8).write(sb.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param verbose
     */
    private void removeApplicationSelector(String applicationName, boolean verbose) {
        String selectorsPath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "selectors";
        String[] versions = new File(selectorsPath).list();
        try {
            for (String version : versions) {
                FileUtils.forceDelete(new File(selectorsPath + File.separator + version + File.separator + applicationName + ".ini"));
                if (verbose) {
                    System.out.println(selectorsPath + " removed with success.");
                }
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param noraRobotName
     * @param verbose
     */
    private void addApplicationInPropertiesFile(String applicationName, String noraRobotName, boolean verbose) {
        manageApplicationInPropertiesFile(true, applicationName, noraRobotName, verbose);
    }

    /**
     * @param applicationName
     * @param noraRobotName
     * @param verbose
     */
    private void removeApplicationInPropertiesFile(String applicationName, String noraRobotName, boolean verbose) {
        manageApplicationInPropertiesFile(false, applicationName, noraRobotName, verbose);
    }

    /**
     * @param addMode
     * @param applicationName
     * @param noraRobotName
     * @param verbose
     */
    private void manageApplicationInPropertiesFile(boolean addMode, String applicationName, String noraRobotName, boolean verbose) {
        String propertiesfilePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + noraRobotName + ".properties";
        if (verbose) {
            System.out.println("Add application named [" + applicationName + "] in this properties file: " + propertiesfilePath + "]");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(propertiesfilePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                if (!(applicationName + "=${" + applicationName + "}").equals(line)) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    if (addMode && "# application list".equals(line)) {
                        sb.append(applicationName + "=${" + applicationName + "}");
                        sb.append(System.lineSeparator());
                    }
                }
                line = br.readLine();
            }
            FileWriter fw = new FileWriter(propertiesfilePath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString().substring(0, sb.toString().length() - System.lineSeparator().length()));
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param url
     * @param env
     * @param verbose
     */
    private void addApplicationInEnvPropertiesFile(String applicationName, String url, String env, boolean verbose) {
        manageApplicationInEnvPropertiesFile(true, applicationName, url, env, verbose);
    }

    /**
     * @param applicationName
     * @param url
     * @param env
     * @param verbose
     */
    private void removeApplicationInEnvPropertiesFile(String applicationName, String env, boolean verbose) {
        manageApplicationInEnvPropertiesFile(false, applicationName, "", env, verbose);
    }

    /**
     * @param addMode
     * @param applicationName
     * @param url
     * @param env
     * @param verbose
     */
    private void manageApplicationInEnvPropertiesFile(boolean addMode, String applicationName, String url, String env, boolean verbose) {
        String propertiesfilePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "environments" + File.separator + env + ".properties";
        if (verbose) {
            System.out.println("Add application named [" + applicationName + "] in this properties file: " + propertiesfilePath + "]");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(propertiesfilePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                if (!line.startsWith(applicationName + "=")) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    if (addMode && "# application list".equals(line)) {
                        sb.append(applicationName + "=" + url);
                        sb.append(System.lineSeparator());
                    }
                }
                line = br.readLine();
            }
            FileWriter fw = new FileWriter(propertiesfilePath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString().substring(0, sb.toString().length() - System.lineSeparator().length()));
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

}
