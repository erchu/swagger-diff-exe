package com.github.erchu.swaggerdiffexe;

import com.deepoove.swagger.diff.SwaggerDiff;
import com.deepoove.swagger.diff.output.HtmlRender;
import lombok.SneakyThrows;
import lombok.val;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.stream.Stream;

class SwaggerDiffReportGenerator {

    void generateHtmlReport(SwaggerDiffReportRequest request) {
        validatePaths(request);

        val swaggerDiff = getSwaggerDiff(
                request.getSwaggerVersion(),
                request.getSwaggerFile1Path().toString(),
                request.getSwaggerFile2Path().toString());
        val reportContent = renderHtmlReport(swaggerDiff);
        saveHtmlReport(reportContent, request.getReportPath());
    }

    private void validatePaths(SwaggerDiffReportRequest request) {
        Stream.of(request.getSwaggerFile1Path(), request.getSwaggerFile2Path())
                .forEach(inputPath -> {
                    val file = inputPath.toFile();

                    if (!file.exists() || !file.canRead() || !file.isFile()) {
                        throw new IllegalArgumentException("File " + file + " don't exist or can be read.");
                    }
                });

        val outputFile = request.getReportPath().toFile();
        val outputDirectory = request.getReportPath().toAbsolutePath().getParent().toFile();

        if (!outputDirectory.exists() || !outputDirectory.canWrite()) {
            throw new IllegalArgumentException("Directory " + outputDirectory + " doesn't exist.");
        }

        //TODO: This method don't cover all possible errors
    }

    private static SwaggerDiff getSwaggerDiff(SwaggerVersion swaggerVersion, String file1, String file2) {
        switch (swaggerVersion) {
            case V1:
                return SwaggerDiff.compareV1(file1, file2);
            case V2:
                return SwaggerDiff.compareV2(file1, file2);
            default:
                throw new IllegalArgumentException("Not supported Swagger version: " + swaggerVersion);
        }
    }

    private static String renderHtmlReport(SwaggerDiff swaggerDiff) {
        return new HtmlRender("Changelog",
                "http://deepoove.com/swagger-diff/stylesheets/demo.css")
                .render(swaggerDiff);
    }

    @SneakyThrows
    private void saveHtmlReport(String reportContent, Path reportPath) {
        val reportFile = reportPath.toFile();

        if (reportFile.exists()) {
            //TODO: It would be nice to ask at this point user is he wants to override file
            //noinspection ResultOfMethodCallIgnored
            reportFile.delete();
        }

        //noinspection ResultOfMethodCallIgnored
        reportPath.toFile().createNewFile();

        val reportFileWriter = new FileWriter(reportPath.toString());
        reportFileWriter.write(reportContent);
        reportFileWriter.close();
    }
}
