@echo off
REM Script para executar o iFome no Windows com suporte a UTF-8

REM Configura o PowerShell para UTF-8
chcp 65001 >nul

echo ========================================
echo      iFome - Sistema de Delivery
echo ========================================
echo.
echo Configurando encoding UTF-8...
echo.

REM Executa o Gradle
call gradlew.bat runApp

echo.
echo ========================================
echo      Programa Encerrado
echo ========================================
pause