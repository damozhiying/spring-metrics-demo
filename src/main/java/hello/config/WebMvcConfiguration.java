package hello.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ExportMetricReader;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.export.MetricExportProperties;
import org.springframework.boot.actuate.metrics.reader.MetricReader;
import org.springframework.boot.actuate.metrics.repository.redis.RedisMetricRepository;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setPropertyCondition((context) -> context.getSource() != null);
        return modelMapper;
    }

    @Bean
    @ExportMetricWriter
    MetricWriter metricWriter(MetricExportProperties export) {
        return new RedisMetricRepository(redisConnectionFactory,
                export.getRedis().getPrefix(), export.getRedis().getKey());
    }

    @Bean
    @ExportMetricReader
    MetricReader metricReader(MetricExportProperties export) {
        return new RedisMetricRepository(redisConnectionFactory,
                export.getRedis().getPrefix(), export.getRedis().getKey());
    }
}