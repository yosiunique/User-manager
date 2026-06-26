package enat.bank.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

    @Value("${spring.application.name:SavingAndLoanRepaymentTracker}")
    private String serviceName;

    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault()
                .toBuilder()
                .put("service.name", serviceName)
                .build();

        OtlpHttpLogRecordExporter logExporter = OtlpHttpLogRecordExporter.builder()
                .setEndpoint("http://host.docker.internal:4318/v1/logs")
                .build();

        LogRecordProcessor processor = BatchLogRecordProcessor.builder(logExporter).build();

        SdkLoggerProvider loggerProvider = SdkLoggerProvider.builder()
                .setResource(resource)
                .addLogRecordProcessor(processor)
                .build();

        return OpenTelemetrySdk.builder()
                .setLoggerProvider(loggerProvider)
                .build();
    }
}

