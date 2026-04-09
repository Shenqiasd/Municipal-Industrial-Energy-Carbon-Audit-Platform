#!/bin/sh
# Custom entrypoint: detect DNS resolver from /etc/resolv.conf,
# inject it into nginx config, then start nginx.
# This is platform-agnostic (works on Docker, Railway, etc.)

# Extract first nameserver from resolv.conf, fallback to 8.8.8.8
RESOLVER=$(awk '/^nameserver/{print $2; exit}' /etc/resolv.conf)
RESOLVER=${RESOLVER:-8.8.8.8}
echo "Detected DNS resolver: $RESOLVER"

# Replace placeholder in nginx config
export DNS_RESOLVER="$RESOLVER"

# Run the default nginx docker-entrypoint to process templates, then start
exec /docker-entrypoint.sh "$@"
