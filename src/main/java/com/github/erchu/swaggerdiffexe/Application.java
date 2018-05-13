package com.github.erchu.swaggerdiffexe;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;

@Slf4j
public class Application {

    private static final String USAGE_MESSAGE = "java -jar swagger-diff-exe.jar v1|v2 swagger_path1 swagger_path2 report_path";

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            log.error("Failed to execute.", e);
        }
    }

    private static void run(String[] args) {
        if (args.length != 4) {
            printUsageInformationAndExit();
        }

        val swaggerVersion = SwaggerVersion.findByStringRepresentation(args[0]);

        if (!swaggerVersion.isPresent()) {
            printUsageInformationAndExit();
        }

        @SuppressWarnings("ConstantConditions") // if Swagger version is not present then we halt application if previous statement
                SwaggerDiffReportRequest reportRequest= SwaggerDiffReportRequest.builder()
                .swaggerVersion(swaggerVersion.get())
                .swaggerFile1Path(Paths.get(args[1]))
                .swaggerFile2Path(Paths.get(args[2]))
                .reportPath(Paths.get(args[3]))
                .build();
        log.info("Arguments: " + StringUtils.join(args, " "));
        new SwaggerDiffReportGenerator().generateHtmlReport(reportRequest);

        log.info("Done");
    }

    private static void printUsageInformationAndExit() {
        log.error("Invalid command line parameters. Usage: " + USAGE_MESSAGE);
        System.exit(1);
    }
}
