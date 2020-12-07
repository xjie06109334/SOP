package com.gitee.app.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.Map;
import java.util.Properties;

/**
 * @author tanghc
 */
public class MyPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);
        if (environment instanceof StandardServletEnvironment) {
            PropertySources appliedPropertySources = this.getAppliedPropertySources();
            for (PropertySource<?> propertySource : appliedPropertySources) {
                Object source = propertySource.getSource();
                if (source instanceof Map) {
                    Map map = (Map)source;
                    map.forEach((key, value)-> {
                        System.setProperty(key.toString(), value.toString());
                    });
                }
            }
        }
    }
}
