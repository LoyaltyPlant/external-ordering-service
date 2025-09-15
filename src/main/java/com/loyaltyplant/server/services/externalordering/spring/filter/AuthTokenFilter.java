package com.loyaltyplant.server.services.externalordering.spring.filter;

import com.loyaltyplant.server.services.externalordering.service.ITokenValidationService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.loyaltyplant.server.services.externalordering.utils.Consts.AUTH_TOKEN_HEADER;
import static com.loyaltyplant.server.services.externalordering.utils.Consts.SALES_OUTLET_HEADER;

@Component
@Log4j2
public class AuthTokenFilter extends OncePerRequestFilter {
    private final ITokenValidationService tokenValidationService;

    @Autowired
    public AuthTokenFilter(final ITokenValidationService tokenValidationService) {
        this.tokenValidationService = tokenValidationService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final @Nonnull HttpServletResponse response,
                                    final @Nonnull FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || !path.startsWith("/api") || path.contains("api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = request.getHeader(AUTH_TOKEN_HEADER);
        final String salesOutletId = request.getHeader(SALES_OUTLET_HEADER);

        if (!StringUtils.hasText(salesOutletId)) {
            LOGGER.error("Request from unknown sales outlet");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            final Integer salesOutletIdInt = Integer.parseInt(salesOutletId);

            if (!StringUtils.hasText(token) || !isValid(salesOutletIdInt, token)) {
                LOGGER.error("Unauthorized request from SO: {}", salesOutletId);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (NumberFormatException ex) {
            LOGGER.error("Bad sales outlet id passed: {}", salesOutletId);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValid(final @Nonnull Integer salesOutletId, final @Nonnull String token) {
        return tokenValidationService.isRequestTokenValid(salesOutletId, token);
    }
}