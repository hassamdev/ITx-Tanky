from django.urls import path
from home.consumer import AppConsumer,TankConsumer,PumpConsumer

ws_urls = [
    path("app-side", AppConsumer.as_asgi()),
    path("tank-side", TankConsumer.as_asgi()),
    path("pump-side", PumpConsumer.as_asgi()),
]