"""
ASGI config for central_module project.

It exposes the ASGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/4.2/howto/deployment/asgi/
"""

import os

from django.core.asgi import get_asgi_application
from channels.routing import ProtocolTypeRouter, URLRouter
from home.routing import ws_urls

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'central_module.settings')

# application = get_asgi_application()



application = ProtocolTypeRouter({
    # WebSocket chat handler
    "websocket": URLRouter(ws_urls)
})
