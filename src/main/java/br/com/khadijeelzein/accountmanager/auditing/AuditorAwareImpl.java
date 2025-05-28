package br.com.khadijeelzein.accountmanager.auditing;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl{// implements AuditorAware<String> {

    /*@Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails user) {
            return Optional.of(user.getUsername());
        }
        return Optional.empty();
    }*/
}