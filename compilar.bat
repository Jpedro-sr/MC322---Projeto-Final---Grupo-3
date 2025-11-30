@echo off



chcp 65001 >nul 2>&1

echo ==================================================
echo      iFome - Sistema de Delivery
echo ==================================================
echo.
echo Configurando ambiente...
echo.

call gradlew.bat --console=plain --quiet runApp

echo.
echo ==================================================
echo      Programa Encerrado
echo ==================================================
pause