package projectCooking.Configure;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
              .setPropertyCondition(Conditions.isNotNull())
              .setMatchingStrategy(MatchingStrategies.STRICT); 

        return mapper;
    }
}
