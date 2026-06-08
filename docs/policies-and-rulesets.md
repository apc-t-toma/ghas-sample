# ポリシーとルールセット

## main ブランチの推奨保護設定

1. マージ前に Pull Request を必須にする
2. 必須ステータスチェックを設定する
3. マージ前に最新ブランチへの更新を必須にする
4. main への直接 push を制限する

必須ステータスチェック:

- CodeQL
- Dependency Review

## 推奨 GHAS 設定

1. Code Scanning を有効化する (デフォルトセットアップまたはワークフロー方式)
2. Dependabot Alerts と Security Updates を有効化する
3. Secret Scanning と Push Protection を有効化する
4. Secret Scanning カスタムパターンをリポジトリ設定に登録する
5. Security Overview を週次で確認する

## マージ基準

1. 承認済み例外がない限り、Critical または High の未対応アラートを残さない
2. 必須チェックがすべて成功している
3. `src/vulnerable-samples` と `.github` 配下の変更は Security Owner 承認を必須にする
4. 例外対応 (抑止またはリスク受容) にはチケット番号、技術的根拠、再評価期限を必須にする

## 段階的強化ロードマップ

1. Phase 1
   - 現行の必須チェックをルールセットに反映
   - 例外申請テンプレートを PR テンプレートへ統合
2. Phase 2
   - Dependency Review のしきい値見直しを実施
   - Dependabot セキュリティ PR を CODEOWNERS レビューへ自動割り当て
3. Phase 3
   - CodeQL の追加クエリを導入
   - 週次 KPI レポート運用を定着
