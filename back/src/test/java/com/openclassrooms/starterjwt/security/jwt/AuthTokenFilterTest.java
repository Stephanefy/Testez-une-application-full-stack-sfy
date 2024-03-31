package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.FilterChain;

import static org.mockito.MockitoAnnotations.openMocks;

class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

//    @Test
//    @Disabled
//    void whenValidJwt_thenAuthenticate() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader("Authorization", "Bearer validToken");
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
//        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username");
//
//        UserDetails userDetails = new User("username", "password", new ArrayList<>());
//        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
//
//        authTokenFilter.doFilterInternal(request, response, filterChain);
//
//        verify(filterChain, times(1)).doFilter(request, response);
//        // Additional assertions can be made here to verify the SecurityContext
//    }

    // Additional tests for invalid JWT or missing JWT...
}
