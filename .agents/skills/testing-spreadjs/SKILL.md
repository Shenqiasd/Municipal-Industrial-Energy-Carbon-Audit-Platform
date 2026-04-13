# Testing SpreadJS Features (Cell Protection, Required Fields, Tag Mapping)

## Overview
This skill covers end-to-end testing of SpreadJS-based features in the Energy Audit Platform, including cell protection, required field marking, and data submission workflows.

## Environment Setup

### Local Dev Stack
- **Backend**: `cd audit-web && mvn spring-boot:run` on port 8080 (H2 in-memory DB)
- **Frontend**: `cd audit-ui && npm run dev` on port 5173 (Vite)
- **Credentials**: admin/Admin@2026 (admin), enterprise/Enterprise@2026 (enterprise)

### Devin Secrets Needed
- No external secrets required for local testing (H2 in-memory DB)
- If testing against production/staging, database credentials would be needed

## Key Testing Techniques

### Element Plus Click Blocking Workaround
Element Plus UI components (el-select, el-button, el-dialog) consistently block Playwright direct clicks due to overlay/wrapper elements intercepting clicks. **Workaround**: Use JavaScript console for ALL Element Plus interactions:

```javascript
// Open dropdown
document.querySelector('.el-select .el-select__wrapper').click();

// Click dropdown option
document.querySelector('[devinid="19"]').click();

// Click buttons by text
const buttons = document.querySelectorAll('button');
for (const btn of buttons) {
  if (btn.textContent.trim() === '提交数据') { btn.click(); break; }
}
```

### Finding SpreadJS Control
The SpreadJS control is NOT found via `canvas.parentElement` hierarchy. Use `gcuielement` attribute:

```javascript
const gcEls = document.querySelectorAll('[gcuielement]');
for (const el of gcEls) {
  const spread = GC.Spread.Sheets.findControl(el);
  if (spread) {
    window._testSpread = spread; // Cache for later use
    break;
  }
}
```

### Inspecting Cell Protection State
```javascript
const sheet = spread.getSheet(0);
sheet.options.isProtected;     // true = sheet protection active
sheet.getCell(r, c).locked();  // true = cell is locked
sheet.getCell(r, c).backColor(); // e.g. '#FFF3E0' for required fields
sheet.comments.get(r, c);     // Comment object (use .text() to read)
sheet.comments.all();         // All comments on sheet
```

### Setting Cell Values Programmatically
```javascript
sheet.setValue(row, col, 'value');
```

## Test Data Setup via API

When using H2 in-memory DB, seed test data via API calls:

1. **Login**: `POST /api/auth/login` with credentials, save token
2. **Set cell tags in designer**: Use SpreadJS API `sheet.setTag(row, col, 'tagName')`
3. **Configure tag mappings**: `PUT /api/template/versions/{id}/tags` with required flags
4. **Publish version**: `POST /api/template/{tplId}/versions/{verId}/publish`
5. **Check protection toggle**: `GET /api/template/versions/{id}` → `protectionEnabled` field

## Common Test Scenarios

### Cell Protection Verification
1. Load template as enterprise user
2. Check `sheet.options.isProtected === true`
3. Verify label cells `locked === true`
4. Verify data-entry cells `locked === false`
5. Verify unmapped cells `locked === true`

### Required Field Marking
1. Check `backColor === '#FFF3E0'` for required cells
2. Check `backColor === undefined` for non-required cells
3. Verify comments exist on required cells with '必填字段' text

### Submission Validation
1. Leave required fields empty, click submit
2. Expect '必填字段未填写' dialog listing missing fields
3. Fill required fields, submit again
4. Expect confirmation dialog, then success message

### Readonly Protection After Submission
1. After successful submission, verify ALL cells are locked
2. Previously unlocked data-entry cells should now be `locked === true`
3. This tests the `applyReadonlyProtection` function

## Browser Session Recovery
After browser restart, Vue Router may infinite-redirect to /login because localStorage is cleared. Fix by setting token first:

```javascript
fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify({username: 'enterprise', password: 'Enterprise@2026'})
}).then(r => r.json()).then(data => {
  localStorage.setItem('token', data.data.token);
  localStorage.setItem('userInfo', JSON.stringify(data.data));
  window.location.href = '/enterprise/report/input';
});
```

## Gotchas
- SpreadJS `getComment()` is not a method on `sheet` — use `sheet.comments.get(row, col)` instead
- `sheet.comments.all()` returns objects where `.row` and `.col` may be undefined; use `.text()` to read content
- H2 in-memory DB loses data on backend restart — re-seed test data if backend was restarted
- The protection feature is gated by `protectionEnabled` on the template version — ensure it's set to 1 before testing
