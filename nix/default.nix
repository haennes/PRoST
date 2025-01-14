self:
{ lib, config, ... }:
let cfg = config.services.prost;
in {
  imports = [ (import ./docker-compose.nix self) ./options.nix ];
  config = lib.mkIf cfg.enable {
    services.nginx = {
      enable = true;
      virtualHosts."localhost".locations."/".root =
        "${cfg.frontend.package}";
    };
  };
}
