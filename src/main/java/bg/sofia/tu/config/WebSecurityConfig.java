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
                .antMatchers("/accounts/getForSelect", "/types/getForSelect", "/priorities/getForSelect", "/resolutions/getForSelect").hasRole("USER")
                .antMatchers("/accounts/**", "/types/**", "/priorities/**", "/resolutions/**").hasRole("ADMIN")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/tasks", true)
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
        auth
                .jdbcAuthentication()
                .dataSource(dataStore)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select username,password,enabled from accounts where username=?")
                .authoritiesByUsernameQuery("select username,role from accounts where username=?");

        auth
                .jdbcAuthentication()
                .dataSource(dataStore)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select email,password,enabled from accounts where email=?")
                .authoritiesByUsernameQuery("select email,role from accounts where email=?");
    }
}