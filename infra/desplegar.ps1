# desplegar.ps1 — Compilar, copiar y reiniciar la API en la instancia
# Uso: .\infra\desplegar.ps1 -Ip <IP_PUBLICA> -Clave <ruta\labsuser.pem>

param(
    [Parameter(Mandatory)] [string]$Ip,
    [Parameter(Mandatory)] [string]$Clave
)

$ErrorActionPreference = "Stop"

Write-Host "=== Compilando ===" -ForegroundColor Cyan
.\mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) { throw "Error en la compilación" }

Write-Host "=== Copiando biblioteca.jar a la instancia ===" -ForegroundColor Cyan
scp -i $Clave "target\biblioteca.jar" "ubuntu@${Ip}:/tmp/"
if ($LASTEXITCODE -ne 0) { throw "Error en scp" }

Write-Host "=== Desplegando y reiniciando ===" -ForegroundColor Cyan
ssh -i $Clave "ubuntu@$Ip" @"
sudo install -o biblioteca -g biblioteca -m 644 /tmp/biblioteca.jar /opt/biblioteca/
sudo systemctl restart biblioteca
sleep 3
systemctl is-active biblioteca
"@

Write-Host "=== Comprobando /api/estado ===" -ForegroundColor Cyan
Start-Sleep -Seconds 5
curl.exe -s -i "http://${Ip}:8080/api/estado"

Write-Host "`n=== Despliegue completado ===" -ForegroundColor Green
