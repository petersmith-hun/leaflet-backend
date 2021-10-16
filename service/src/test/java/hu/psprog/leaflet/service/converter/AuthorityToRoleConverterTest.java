package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.service.common.Authority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link AuthorityToRoleConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AuthorityToRoleConverterTest {

    @InjectMocks
    private AuthorityToRoleConverter converter;

    @Test
    public void shouldConvert() {

        // given
        GrantedAuthority authority = Authority.ADMIN;

        // when
        Role result = converter.convert(authority);

        // then
        assertThat(result, equalTo(Role.ADMIN));
    }
}