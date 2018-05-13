package com.github.erchu.swaggerdiffexe;

import lombok.Builder;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
class SwaggerDiffReportRequest {

    private final SwaggerVersion swaggerVersion;
    private final Path swaggerFile1Path;
    private final Path swaggerFile2Path;
    private final Path reportPath;
}
