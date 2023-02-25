module.exports = {
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint', 'prettier'],
  extends: ['prettier', 'standard'],
  rules: {
    'prettier/prettier': 'warn',
    'no-use-before-define': 'off',
    'space-before-function-paren': 'off',
    'no-unused-vars': 'off',
    '@typescript-eslint/no-unused-vars': ['warn'],
    semi: 'warn',
    'no-undef': 'off',
    ident: 'off',
    'multiline-ternary': 'off'
  }
}
