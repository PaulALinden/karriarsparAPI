package se.karriarspar.karriarsparAPI.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();
    private final Map<String, BlockInfo> blockedIps = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final int MAX_STRIKES = 3;
    private static final int BLOCK_DURATION_SECONDS = 600;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        long now = Instant.now().getEpochSecond();

        if (blockedIps.containsKey(ip)) {
            BlockInfo block = blockedIps.get(ip);
            if (now < block.blockedUntil) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Du är tillfälligt blockerad. Försök igen senare.");
                return;
            } else {
                blockedIps.remove(ip);
            }
        }

        RequestInfo info = requests.computeIfAbsent(ip, k -> new RequestInfo());

        synchronized (info) {
            if (now - info.timestamp >= 60) {
                info.timestamp = now;
                info.counter = 1;
            } else {
                info.counter++;
            }

            if (info.counter > MAX_REQUESTS_PER_MINUTE) {
                info.strikes++;

                System.out.println("Överträdelse från IP: " + ip + " (Strike " + info.strikes + ")");

                if (info.strikes >= MAX_STRIKES) {
                    long blockUntil = now + BLOCK_DURATION_SECONDS;
                    blockedIps.put(ip, new BlockInfo(blockUntil));
                    System.out.println("IP blockerat i 10 minuter: " + ip);
                }

                response.setStatus(HttpServletResponse.SC_CONFLICT);//409
                response.getWriter().write("För många förfrågningar, försök senare.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    static class RequestInfo {
        long timestamp = Instant.now().getEpochSecond();
        int counter = 0;
        int strikes = 0;
    }

    static class BlockInfo {
        long blockedUntil;
        BlockInfo(long until) {
            this.blockedUntil = until;
        }
    }
}
