# GeRDI Harvester Image for 'ArcGIS'

FROM jetty:9.4.7-alpine

COPY \/target\/*.war $JETTY_BASE\/webapps\/arcgis.war

EXPOSE 8080