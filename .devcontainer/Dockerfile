# syntax=docker/dockerfile:1

FROM mcr.microsoft.com/devcontainers/base:ubuntu-24.04

ENV TZ=Asia/Tokyo

SHELL ["/bin/bash", "-euo", "pipefail", "-c"]

USER vscode

# タイムゾーンとロケールの設定
RUN --mount=type=cache,target=/var/cache/apt,sharing=locked \
  --mount=type=cache,target=/var/lib/apt/lists,sharing=locked \
  --mount=type=tmpfs,target=/tmp \
  <<EOF
export DEBIAN_FRONTEND=noninteractive

sudo apt-get update -qq
sudo apt-get install -qq -y \
  curl \
  locales \
  ripgrep \
  tzdata

# ロケールのセットアップ
sudo locale-gen ja_JP.UTF-8

# google-chrome インストール (https://www.google.com/linuxrepositories/)
wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo tee /etc/apt/trusted.gpg.d/google.asc >/dev/null
echo "deb [arch=amd64 signed-by=/etc/apt/trusted.gpg.d/google.asc] http://dl.google.com/linux/chrome/deb/ stable main" | sudo tee /etc/apt/sources.list.d/google-chrome.list

sudo apt-get update -qq
sudo apt-get install -qq -y google-chrome-stable

# mise インストール（https://mise.jdx.dev/getting-started.html#installing-mise-cli）
sudo install -dm 755 /etc/apt/keyrings
curl -fSs https://mise.jdx.dev/gpg-key.pub | sudo tee /etc/apt/keyrings/mise-archive-keyring.asc 1> /dev/null
echo "deb [signed-by=/etc/apt/keyrings/mise-archive-keyring.asc] https://mise.jdx.dev/deb stable main" | sudo tee /etc/apt/sources.list.d/mise.list
sudo apt-get update -qq
sudo apt-get install -qq -y mise

echo 'eval "$(mise activate bash)"' >> ~/.bashrc

# 不要なパッケージを削除
sudo apt-get autoremove -y

# MCPサーバー実行のためグローバルインストール
# miseのcore pluginでnodeをインストールすると、gitのgpgエラーが発生するため、asdfのnodejs pluginを使用してインストールする
# （miseの不具合の可能性あり、miseのアップデートで修正されたらcore pluginを使用）
mise plugins install node https://github.com/asdf-vm/asdf-nodejs
mise use --global node@24.16

EOF

# ロケール環境変数の設定
ENV LANG=ja_JP.UTF-8
