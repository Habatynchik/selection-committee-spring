package ua.epan.elearn.selection.committee.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.epan.elearn.selection.committee.spring.model.entity.Role;
import ua.epan.elearn.selection.committee.spring.model.service.UserService;

import javax.annotation.Resource;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SESSION_COOKIE = "JSESSIONID";

    private final UserService userService;

    public SecurityConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/user/**").hasAnyAuthority(Role.RoleEnum.CLIENT.name(), Role.RoleEnum.ADMIN.name())
                .antMatchers("/admin/**").hasAuthority(Role.RoleEnum.ADMIN.name())
                .antMatchers("/registration").anonymous()
                .antMatchers("/login").anonymous()
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
              //  .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
              //  .logoutSuccessUrl("/login")
                .deleteCookies(SESSION_COOKIE)
                .invalidateHttpSession(true);

    }


}
