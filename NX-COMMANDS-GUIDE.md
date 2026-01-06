# Nx Commands Guide for Maven Monorepository

This guide shows you how to use Nx to intelligently build and test only what changed in your monorepository.

## 🎯 Benefits of Using Nx

1. **Affected Analysis** - Only build/test changed projects
2. **Dependency Graph** - Visualize project relationships
3. **Task Caching** - Skip redundant builds
4. **Parallel Execution** - Run tasks concurrently

## 📦 Project Configuration

Each project now has a `project.json` file that defines:
- **Project metadata** (name, type, source root)
- **Build targets** (build, test, serve, clean)
- **Dependencies** (implicit and explicit)
- **Tags** (for filtering and organizing)

## 🚀 Essential Nx Commands

### 1. Check What's Affected

See which projects are affected by your changes:

```bash
# Show affected projects
./nx affected:graph

# List affected projects
./nx affected:apps
./nx affected:libs

# Show all affected projects
./nx print-affected
```

### 2. Build Only What Changed

Build only the projects affected by your changes:

```bash
# Build affected projects only
./nx affected -t build

# Build affected projects with verbose output
./nx affected -t build --verbose

# Build affected projects in parallel
./nx affected -t build --parallel=3
```

### 3. Test Only What Changed

Run tests only for affected projects:

```bash
# Test affected projects only
./nx affected -t test

# Test with specific base branch
./nx affected -t test --base=main

# Test with specific head
./nx affected -t test --base=main --head=HEAD
```

### 4. Run Specific Project Tasks

Run tasks for a specific project:

```bash
# Build utility-library
./nx build utility-library

# Build service-a
./nx build service-a

# Build service-b
./nx build service-b

# Run service-a
./nx serve service-a

# Run service-b
./nx serve service-b

# Test service-a
./nx test service-a

# Clean service-a
./nx clean service-a
```

### 5. Run All Projects

Build or test all projects:

```bash
# Build all projects
./nx run-many -t build --all

# Test all projects
./nx run-many -t test --all

# Clean all projects
./nx run-many -t clean --all
```

### 6. Run Multiple Targets

Run multiple targets at once:

```bash
# Clean and build all projects
./nx run-many -t clean,build --all

# Build and test affected projects
./nx affected -t build,test
```

## 🎨 Visualize Project Graph

See the dependency relationships between projects:

```bash
# Open interactive graph in browser
./nx graph

# Show affected project graph
./nx affected:graph

# Generate static graph
./nx graph --file=output.html
```

**Expected Graph Structure:**
```
service-a  ──→  utility-library
service-b  ──→  utility-library
```

## 🔍 Real-World Scenarios

### Scenario 1: Changed Only service-a

```bash
# You modified files in service-a only

# Check what's affected
./nx affected:apps
# Output: service-a

# Build only service-a
./nx affected -t build
# Will build: service-a only (not service-b!)

# Test only service-a
./nx affected -t test
# Will test: service-a only
```

### Scenario 2: Changed Only service-b

```bash
# You modified files in service-b only

# Build only service-b
./nx affected -t build
# Will build: service-b only (not service-a!)
```

### Scenario 3: Changed utility-library

```bash
# You modified files in utility-library

# Check what's affected
./nx affected:graph
# Will show: utility-library, service-a, and service-b

# Build affected projects
./nx affected -t build
# Will build: utility-library, service-a, and service-b
# (because both services depend on utility-library)
```

### Scenario 4: Changed Only Documentation

```bash
# You modified only README.md

# Check what's affected
./nx affected:apps
# Output: (none)

# Build affected projects
./nx affected -t build
# Will build: nothing! (saves time)
```

## 🎯 Comparing with Different Bases

By default, Nx compares with the `main` branch. You can specify different bases:

```bash
# Compare with origin/main
./nx affected -t build --base=origin/main

# Compare with a specific commit
./nx affected -t build --base=abc123

# Compare between two commits
./nx affected -t build --base=abc123 --head=def456

# Compare with uncommitted changes
./nx affected -t build --base=HEAD
```

## 📊 Project Tags

Projects are tagged for easy filtering:

### Utility Library
- `type:library`
- `scope:shared`

### Service A
- `type:app`
- `platform:jvm`
- `service:user-management`

### Service B
- `type:app`
- `platform:jvm`
- `service:order-management`

### Use tags to filter:

```bash
# Build all apps
./nx run-many -t build --projects=tag:type:app

# Build all libraries
./nx run-many -t build --projects=tag:type:library

# Build all JVM projects
./nx run-many -t build --projects=tag:platform:jvm
```

## 🚀 Practical Workflow

### Daily Development Workflow

```bash
# 1. Start working on service-a
cd service-a
# ... make changes ...

# 2. Build only what changed
./nx affected -t build

# 3. Test only what changed
./nx affected -t test

# 4. Run service-a
./nx serve service-a

# 5. Before committing, check everything affected
./nx affected:graph
```

### Feature Branch Workflow

```bash
# 1. Create feature branch
git checkout -b feature/add-user-validation

# 2. Make changes to service-a and utility-library
# ... edit files ...

# 3. Build affected projects
./nx affected -t build --base=origin/main

# 4. Test affected projects
./nx affected -t test --base=origin/main

# 5. View what will be affected in PR
./nx affected:graph --base=origin/main

# 6. Commit and push
git add .
git commit -m "Add user validation"
git push origin feature/add-user-validation
```

## 💡 Performance Tips

### 1. Use Caching

Nx automatically caches build and test results. Subsequent runs are much faster!

```bash
# First run (no cache)
./nx test service-a
# Takes: ~10 seconds

# Second run (with cache)
./nx test service-a
# Takes: ~1 second (from cache!)
```

### 2. Parallel Execution

Run multiple projects in parallel:

```bash
# Build all projects in parallel
./nx run-many -t build --all --parallel=3

# Build affected projects in parallel
./nx affected -t build --parallel=2
```

### 3. Skip Cache

Force rebuild without cache:

```bash
./nx build service-a --skip-nx-cache
```

## 🔧 Troubleshooting

### Reset Nx Cache

If you have cache issues:

```bash
./nx reset
```

### View Nx Daemon Status

```bash
./nx daemon --status
```

### Stop Nx Daemon

```bash
./nx daemon --stop
```

## 📈 CI/CD Integration

In your CI/CD pipeline (GitHub Actions, GitLab CI, etc.):

```yaml
# Example GitHub Actions workflow
- name: Build affected projects
  run: ./nx affected -t build --base=origin/main --head=HEAD

- name: Test affected projects
  run: ./nx affected -t test --base=origin/main --head=HEAD
```

This ensures CI only builds and tests what changed, saving time and resources!

## 🎓 Key Concepts

### Affected Analysis

Nx analyzes:
1. **File changes** (git diff)
2. **Project dependencies** (from project.json)
3. **Implicit dependencies** (specified in project.json)

### Dependency Graph

```
utility-library (changed)
    ↓ (affects)
    ├── service-a (must rebuild)
    └── service-b (must rebuild)
```

If you change `utility-library`, both services are "affected" because they depend on it.

### Caching

Nx caches based on:
- Input files
- Configuration
- Dependencies

If nothing changed, it uses the cached result instead of rebuilding.

## 📋 Quick Reference

| Command | Description |
|---------|-------------|
| `./nx affected:graph` | Show affected project graph |
| `./nx affected -t build` | Build affected projects |
| `./nx affected -t test` | Test affected projects |
| `./nx build <project>` | Build specific project |
| `./nx serve <project>` | Run specific project |
| `./nx test <project>` | Test specific project |
| `./nx run-many -t build --all` | Build all projects |
| `./nx graph` | View full project graph |
| `./nx reset` | Clear Nx cache |

## 🎯 When to Use What

| Situation | Command |
|-----------|---------|
| Changed only service-a | `./nx affected -t build` |
| Changed utility-library | `./nx affected -t build` |
| Want to build everything | `./nx run-many -t build --all` |
| Working on specific service | `./nx build service-a` |
| Before creating PR | `./nx affected:graph --base=origin/main` |
| CI/CD pipeline | `./nx affected -t build,test --base=origin/main` |

## 🚀 Advanced Usage

### Run with Specific Configuration

```bash
# Run with production configuration
./nx build service-a --configuration=production

# Run with specific environment
./nx serve service-a --port=9090
```

### Generate Dependency Report

```bash
# Generate JSON report
./nx print-affected --type=app --target=build --select=projects > affected.json
```

## 🎉 Summary

With Nx configured, you can now:

✅ **Build only what changed** - Save time in development  
✅ **Test only affected code** - Faster feedback loop  
✅ **Visualize dependencies** - Understand project relationships  
✅ **Cache build results** - Instant rebuilds when nothing changed  
✅ **Run tasks in parallel** - Maximum efficiency  
✅ **Optimize CI/CD** - Only build/test affected projects in PRs  

This makes your monorepository development experience **10x faster**! 🚀

---

**Next Steps:**
1. Try `./nx graph` to see your project structure
2. Make a change to service-a and run `./nx affected:graph`
3. Build only affected projects with `./nx affected -t build`
4. Enjoy faster development! 🎉
