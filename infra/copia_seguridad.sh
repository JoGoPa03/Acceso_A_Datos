#!/bin/bash
# copia_seguridad.sh — Copia lógica diaria de la base de datos biblioteca
# Instalar en /usr/local/sbin/copia_seguridad.sh
# Programar con: sudo crontab -e → 15 3 * * * /usr/local/sbin/copia_seguridad.sh

set -euo pipefail

DIR_BACKUP="/var/backups/biblioteca"
FECHA=$(date +%Y%m%d_%H%M%S)
FICHERO="${DIR_BACKUP}/biblioteca_${FECHA}.sql.gz"
DIAS_RETENCION=7

mkdir -p "$DIR_BACKUP"

mysqldump --single-transaction --routines --triggers \
    -u biblioteca_admin -p"$(cat /etc/biblioteca/.db_admin_pass)" \
    biblioteca | gzip > "$FICHERO"

chmod 600 "$FICHERO"

find "$DIR_BACKUP" -name "biblioteca_*.sql.gz" -mtime +"$DIAS_RETENCION" -delete

echo "$(date '+%Y-%m-%d %H:%M:%S') Copia completada: $FICHERO ($(du -h "$FICHERO" | cut -f1))"
