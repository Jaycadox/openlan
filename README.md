# openlan
Allows friends to connect to your LAN world whilst not sharing the same internet connection. In addition, this hides your IP address from both the server owner, and to anyone connecting to the server.

Only the player wishing to host the server needs to have the OpenLAN mod installed. All others players can play without any modifications.

# How to use
Just click "Open to LAN" in your Minecraft settings menu on your singleplayer world, soon after, you'll get a notification with a domain that other players can use to connect to your world (via the Multiplayer menu).

# About
## How is the server hosted?
`on.jayphen.xyz` is running a very lightly modified version of [HyperTunnel](https://github.com/berstend/hypertunnel). The only modifications are default settings.

## How is the client hosted?
The client side tunnel program (which can be found [here](https://github.com/Jaycadox/openlan-files)) is a very lightly modified version of the HyperTunnel client (once again, wiith default settings changed). This has been packaged with [pkg](https://github.com/vercel/pkg) for NodeJS.

## Misc
The `bootstrap` files are generated depending on what operating system you are on. This is to guarantee a capturing of STDOUT on the tunnel process.

With the tunnel software, you can tunnel/expose any TCP socket on your system. Just run either: `tunnel.exe --port 1234` or `./tunnel --port 1234` depending on your OS. The next line on STDOUT will either be an error or the corresponding, newly generated domain name.

The tunnel server is hosted in Sydney, Australia.
