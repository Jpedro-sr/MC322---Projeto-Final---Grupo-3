@echo off
REM Script para executar o iFome no Windows SEM TIMER do Gradle

REM Configura UTF-8
chcp 65001 >nul 2>&1

echo ==================================================
echo      iFome - Sistema de Delivery
echo ==================================================
echo.
echo Configurando ambiente...
echo.

REM IMPORTANTE: Usa --console=plain para remover o timer
call gradlew.bat --console=plain --quiet runApp

echo.
echo ==================================================
echo      Programa Encerrado
echo ==================================================
pause