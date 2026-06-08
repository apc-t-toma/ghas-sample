#!/usr/bin/env bash

set -euo pipefail

mise trust
mise install

# GitHub CLI 認証
echo "${GITHUB_PAT}" | gh auth login --with-token

# Antigravity CLI インストール
curl -fsSL https://antigravity.google/cli/install.sh | bash

# Claude Code インストール
curl -fsSL https://claude.ai/install.sh | bash

# GitHub Copilot CLI インストール
curl -fsSL https://gh.io/copilot-install | bash
