package bg.sofia.tu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 22/08/2016 @ 12:57.
 */
@Configuration
@PropertySource(value = {"classpath:application.properties"}, ignoreResourceNotFound = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private DriverManagerDataSource dataStore;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/users/**", "/tournaments/**", "/results/**", "/marketing/**", "/notifications/**", "/mobile_configuration/**").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/account/**").hasRole("USER")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/task/list", true)
                .permitAll()

                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/img/**", "/css/**", "/js/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        if (adminUsername == null || adminUsername.length() == 0) {
//            adminUsername = "admin";
//        }
//        if (adminPassword == null || adminPassword.length() == 0) {
//            adminPassword = "admin";
//        }
//        auth
//                .inMemoryAuthentication()
//                .withUser(adminUsername).password(adminPassword).roles("ADMIN");
        auth
                .jdbcAuthentication()
                .dataSource(dataStore)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select username,password,enabled from account where username=?")
                .authoritiesByUsernameQuery("select username,role from account where username=?");
    }
}