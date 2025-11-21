# Script de compilacao para PowerShell
# Salvar este arquivo na RAIZ do projeto como: compilar.ps1

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "  Compilando iFome - Projeto MC322" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# Criar diretorio bin se nao existir
if (-Not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

# Limpar compilacao anterior
Remove-Item "bin\*" -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "[1/4] Compilando exceptions..." -ForegroundColor Yellow
javac -encoding UTF-8 -d bin src\main\java\ifome\exceptions\*.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO ao compilar exceptions!" -ForegroundColor Red
    exit 1
}

Write-Host "[2/4] Compilando model..." -ForegroundColor Yellow
javac -encoding UTF-8 -d bin -cp bin src\main\java\ifome\model\*.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO ao compilar model!" -ForegroundColor Red
    exit 1
}

Write-Host "[3/4] Compilando util..." -ForegroundColor Yellow
javac -encoding UTF-8 -d bin -cp bin src\main\java\ifome\util\*.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO ao compilar util!" -ForegroundColor Red
    exit 1
}

Write-Host "[4/4] Compilando aplicacao..." -ForegroundColor Yellow
javac -encoding UTF-8 -d bin -cp bin src\main\java\ifome\*.java
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO ao compilar aplicacao!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "====================================" -ForegroundColor Green
Write-Host "  Compilacao concluida com sucesso!" -ForegroundColor Green
Write-Host "====================================" -ForegroundColor Green
Write-Host ""
Write-Host "Para executar: .\executar.ps1" -ForegroundColor Cyan
Write-Host ""