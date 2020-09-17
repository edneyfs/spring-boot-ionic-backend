package com.efs.cursomc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("docker") // serão ativados apenas quando o profile estiver definido com este name
public class DockerConfig extends DevConfig {

}