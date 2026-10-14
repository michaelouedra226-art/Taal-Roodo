#!/usr/bin/env bash
set -e

# Assurer les permissions d'exécution pour gradlew et les scripts
chmod +x gradlew 2>/dev/null || true
if [ -d ".git" ]; then
  git update-index --chmod=+x gradlew 2>/dev/null || true
fi

echo "Permissions de gradlew corrigées avec succès."
