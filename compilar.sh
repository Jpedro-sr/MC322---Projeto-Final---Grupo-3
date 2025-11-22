#!/bin/bash
# Script para executar o iFome no Linux/Mac SEM TIMER do Gradle

echo "=================================================="
echo "      iFome - Sistema de Delivery"
echo "=================================================="
echo ""
echo "Configurando ambiente..."
echo ""

# IMPORTANTE: Usa --console=plain para remover o timer
./gradlew --console=plain --quiet runApp

echo ""
echo "=================================================="
echo "      Programa Encerrado"
echo "=================================================="