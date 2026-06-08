# GHAS 検証ガイド

## 1. Code Scanning (CodeQL)

1. feature ブランチを作成して変更を push する
2. `main` 向け Pull Request を作成する
3. `CodeQL` ワークフローが実行されることを確認する
4. `src/vulnerable-samples/kotlin` 配下を中心にアラートを確認する

期待結果:

- Security タブと PR チェックにアラートが表示される

## 2. Dependabot アラートと更新

1. `.github/dependabot.yml` が存在することを確認する
2. スケジュール実行を待つか、UI から Dependabot を手動実行する
3. 既知脆弱性のある依存関係でアラートが作成されることを確認する
4. 更新 PR が生成されることを確認する

期待結果:

- Dependabot アラートと更新 PR が対象パッケージに紐付く

## 3. Pull Request での Dependency Review

1. `build.gradle.kts` の依存関係を追加または更新する
2. Pull Request を作成する
3. `Dependency Review` チェックが実行されることを確認する

期待結果:

- ポリシー閾値を超える依存関係リスクがある場合、PR が失敗または警告となる

## 4. Secret Scanning / Push Protection

1. `test-data/secrets/fake-secrets.txt` の疑似パターンのみを使用する
2. ローカル検証ファイルは `test-data/secrets/local/` に置く
3. `.github/secret-scanning/custom-patterns.json` のパターンをリポジトリ設定に登録する
4. Push Protection を有効化する

期待結果:

- 登録済みパターンに一致する疑似シークレットが検知される
- 保護対象ブランチへの push がブロックされる

## 5. Security Report ワークフロー

1. `Security Report` ワークフローを手動実行する
2. アーティファクト `ghas-security-report` をダウンロードする
3. `code-scanning-alerts.json` と `dependabot-alerts.json` を確認する
4. `security-kpi-summary.json` を確認する

期待結果:

- UI 外でもアラート状態を時系列で追跡できる
- 重大度別件数を週次で把握できる

## 6. 運用強化の確認項目

1. 必須チェックがブランチ保護に設定済みである
2. 例外にチケット番号と期限が必須化されている
3. Dependabot セキュリティ PR が Security Owner へレビュー依頼される
4. 週次 KPI を継続確認している
