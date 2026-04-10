# SpreadJS & Frontend Access

## SpreadJS License Domain

The SpreadJS license key is bound to the domain `sjs.ben-china.org.cn`. 

- **MUST** access the frontend via `https://sjs.ben-china.org.cn` for SpreadJS functionality to work.
- Accessing via the Railway domain (`audit-ui-production-2902.up.railway.app`) will cause SpreadJS v18.2.5 to become **completely non-functional** (not just watermarked) — `fromJSON`, `getSheetCount`, etc. will all throw errors like "Cannot set properties of null (setting 'appDocProps')".
- The license file is at `audit-ui/src/utils/spreadjs-license.ts` with hardcoded keys.
- The license is a temporary deployment key — check expiration status in the SpreadJS footer bar.

## Frontend URLs

- **Production frontend (with SpreadJS)**: `https://sjs.ben-china.org.cn`
- **Railway direct URL**: `https://audit-ui-production-2902.up.railway.app` (SpreadJS will NOT work here)

## Backend URLs

- **Production backend API**: `https://energyauditplatform-production.up.railway.app/api`

## Test Credentials

- **Enterprise login**: username `AS2335234234252223`, password stored in secrets (enterprise_id=4)

## Deployment

- Frontend and backend are deployed on Railway.
- Both services have GitHub auto-deploy configured on the `master` branch.
- To trigger redeployment: push/merge to `master` and Railway will auto-build and deploy.
