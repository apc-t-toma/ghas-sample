# ghas-sample (Kotlin + Gradle via mise)

このリポジトリは、Kotlin を使って GitHub Advanced Security の機能をエンドツーエンドで検証するためのサンプルです。

## このプロジェクトで検証する内容

1. CodeQL によるコードスキャン (Kotlin/Java)
2. Secret Scanning と Push Protection
3. Gradle 依存関係に対する Dependabot アラートとセキュリティ更新
4. Pull Request における Dependency Review チェック
5. 手動実行および定期実行によるセキュリティ結果レポート収集

## ツールチェーン

- 言語: Kotlin (JVM)
- ビルド: Gradle (Kotlin DSL)
- ランタイム管理: mise

## クイックスタート

1. mise でツールチェーンをインストールする
1. セットアップ確認を実行する

```bash
mise run setup
```

1. ビルドとテストを実行する

```bash
mise run build
mise run test
```

## 重要な安全ルール

1. 実シークレットは絶対にコミットしない
2. `src/vulnerable-samples` 配下のコードは意図的に脆弱なため、本番で再利用しない
3. ローカル限定のシークレット検証は `test-data/secrets/local/` に保存する

## 主要ファイル

- `.github/workflows/codeql.yml`: CodeQL 解析ワークフロー
- `.github/dependabot.yml`: Dependabot 更新設定
- `.github/workflows/dependency-review.yml`: PR 依存関係リスクゲート
- `.github/workflows/security-report.yml`: セキュリティアラート収集レポート
- `.github/secret-scanning/custom-patterns.json`: Secret Scanning カスタムパターン定義テンプレート
- `docs/ghas-verification-guide.md`: 検証手順
- `docs/policies-and-rulesets.md`: 推奨ポリシーとルールセット
- `docs/triage-playbook.md`: トリアージ運用手順
