# Auto-generated using compose2nix v0.2.3.
{ pkgs, lib, config, ... }:
let cfg = config.services.prost;
in {
  config = {
    # Runtime
    virtualisation.docker = {
      enable = true;
      autoPrune.enable = true;
    };
    virtualisation.oci-containers.backend = "docker";

    # Containers
    virtualisation.oci-containers.containers."PRoST-Backend" = {
      image = "prost-backend";
      environment = { "LDAP_URI" = cfg.ldapUri; };
      volumes = [ "${cfg.backend.volumePath}:/data:rw" ];
      ports = [ "8081:8081/tcp" ];
      dependsOn = [ "PRoST-LDAP" "mariadb" ];
      log-driver = "journald";
      extraOptions = [ "--network-alias=backend" "--network=prost_prost" ];
    };
    systemd.services."docker-PRoST-Backend" = {
      serviceConfig = { Restart = lib.mkOverride 500 "no"; };
      after = [ "docker-network-prost_prost.service" ];
      requires = [ "docker-network-prost_prost.service" ];
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };
    virtualisation.oci-containers.containers."PRoST-Frontend" = {
      #image = "prost-frontend";
      image = "frontend:latest";
      imageFile = cfg.frontend.package;
      ports = [ "${toString cfg.port}:80/tcp" ];
      log-driver = "journald";
      extraOptions = [ "--network-alias=frontend" "--network=prost_default" ];
    };
    systemd.services."docker-PRoST-Frontend" = {
      serviceConfig = { Restart = lib.mkOverride 500 "no"; };
      after = [ "docker-network-prost_default.service" ];
      requires = [ "docker-network-prost_default.service" ];
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };
    virtualisation.oci-containers.containers."PRoST-LDAP" = with cfg.ldap; {
      image = "bitnami/openldap:latest";
      environment = {
        "LDAP_ADMIN_DN" = admin.dn;
        "LDAP_ADMIN_PASSWORD" = admin.password;
        "LDAP_ADMIN_USERNAME" = admin.username;
        "LDAP_CUSTOM_LDIF_DIR" = "/ldif";
        "LDAP_ROOT" = root;
      };
      volumes = [ "${dir}:/ldif:rw" "prost_prost-ldap:/bitnami/openldap:rw" ];
      log-driver = "journald";
      extraOptions = [ "--network-alias=ldap" "--network=prost_prost" ];
    };
    systemd.services."docker-PRoST-LDAP" = {
      serviceConfig = { Restart = lib.mkOverride 500 "no"; };
      after = [
        "docker-network-prost_prost.service"
        "docker-volume-prost_prost-ldap.service"
      ];
      requires = [
        "docker-network-prost_prost.service"
        "docker-volume-prost_prost-ldap.service"
      ];
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };
    virtualisation.oci-containers.containers."mariadb" = with cfg.mariadb; {
      image = "lscr.io/linuxserver/mariadb:latest";
      environment = {
        "MYSQL_DATABASE" = database;
        "MYSQL_PASSWORD" = password;
        "MYSQL_ROOT_PASSWORD" = root_password;
        "MYSQL_USER" = user;
      };
      volumes = [ "${configDir}:/config:rw" ];
      log-driver = "journald";
      extraOptions = [ "--network-alias=mariadb" "--network=prost_prost" ];
    };
    systemd.services."docker-mariadb" = {
      serviceConfig = { Restart = lib.mkOverride 500 "no"; };
      after = [ "docker-network-prost_prost.service" ];
      requires = [ "docker-network-prost_prost.service" ];
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };

    # Networks
    systemd.services."docker-network-prost_default" = {
      path = [ pkgs.docker ];
      serviceConfig = {
        Type = "oneshot";
        RemainAfterExit = true;
        ExecStop = "docker network rm -f prost_default";
      };
      script = ''
        docker network inspect prost_default || docker network create prost_default
      '';
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };
    systemd.services."docker-network-prost_prost" = {
      path = [ pkgs.docker ];
      serviceConfig = {
        Type = "oneshot";
        RemainAfterExit = true;
        ExecStop = "docker network rm -f prost_prost";
      };
      script = ''
        docker network inspect prost_prost || docker network create prost_prost
      '';
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };

    # Volumes
    systemd.services."docker-volume-prost_prost-ldap" = {
      path = [ pkgs.docker ];
      serviceConfig = {
        Type = "oneshot";
        RemainAfterExit = true;
      };
      script = ''
        docker volume inspect prost_prost-ldap || docker volume create prost_prost-ldap
      '';
      partOf = [ "docker-compose-prost-root.target" ];
      wantedBy = [ "docker-compose-prost-root.target" ];
    };

    # Root service
    # When started, this will automatically create all resources and start
    # the containers. When stopped, this will teardown all resources.
    systemd.targets."docker-compose-prost-root" = {
      unitConfig = { Description = "Root target generated by compose2nix."; };
      wantedBy = [ "multi-user.target" ];
    };
  };
}
