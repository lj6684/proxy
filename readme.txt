A proxy implements with MINA NIO

general proxy mode:
(set connectionMode=none)
request --> proxy --> target

ssl encryption proxy mode:
(need to proxy programme
(set connectionMode=client/server)
request --> clientProxy ==SSL==> serverProxy --> target