import os
import sys
from collections import defaultdict

def count_lines(filepath, encoding='utf-8'):
    """Counts lines in a file, handling potential encoding errors."""
    try:
        with open(filepath, 'r', encoding=encoding, errors='ignore') as f:
            return sum(1 for _ in f)
    except Exception as e:
        print(f"Warning: Could not read file {filepath}: {e}", file=sys.stderr)
        return 0

def analyze_project(root_dir='.', excluded_dirs=None, target_extensions=None):
    """
    Analyzes the project structure for specified file types, excluding certain directories.

    Args:
        root_dir (str): The root directory of the project to scan.
        excluded_dirs (set): A set of directory names to exclude.
        target_extensions (dict): A dictionary mapping extensions to language names.

    Returns:
        tuple: (stats_by_dir, totals)
               - stats_by_dir: Dict storing counts per top-level directory.
               - totals: Dict storing overall counts for each language.
    """
    if excluded_dirs is None:
        excluded_dirs = {'node_modules', 'target', '.git', '.idea', 'dist', 'build'}
    if target_extensions is None:
        target_extensions = {'.java': 'Java', '.ts': 'TypeScript'}

    # Structure: stats[top_level_dir][language]['files' | 'lines']
    stats_by_dir = defaultdict(lambda: defaultdict(lambda: defaultdict(int)))
    # Structure: totals[language]['files' | 'lines']
    totals = defaultdict(lambda: defaultdict(int))

    print(f"Scanning project structure in: {os.path.abspath(root_dir)}")
    print(f"Excluding directories: {', '.join(sorted(excluded_dirs))}")
    print(f"Counting files with extensions: {', '.join(target_extensions.keys())}\n")

    for current_root, dirs, files in os.walk(root_dir, topdown=True):
        # Modify dirs in-place to prevent os.walk from descending into excluded directories
        dirs[:] = [d for d in dirs if d not in excluded_dirs]

        # Ensure we don't process files within an excluded directory at the root level
        # (os.walk starts at root_dir, so the initial dirs filtering works for subdirs,
        # but we need to check the starting current_root as well)
        relative_root = os.path.relpath(current_root, root_dir)
        if relative_root != '.':
             root_parts = relative_root.split(os.path.sep)
             if any(part in excluded_dirs for part in root_parts):
                 continue # Skip processing files if any part of the path is excluded

        for file in files:
            _, extension = os.path.splitext(file)

            if extension in target_extensions:
                language = target_extensions[extension]
                full_path = os.path.join(current_root, file)
                relative_path = os.path.relpath(full_path, root_dir)

                # Determine top-level directory or file
                path_parts = relative_path.split(os.path.sep)
                top_level = path_parts[0] if path_parts else relative_path

                # Count lines
                line_count = count_lines(full_path)

                # Update stats
                stats_by_dir[top_level][language]['files'] += 1
                stats_by_dir[top_level][language]['lines'] += line_count
                totals[language]['files'] += 1
                totals[language]['lines'] += line_count

    return stats_by_dir, totals

# --- Main Execution ---
if __name__ == "__main__":
    stats_by_directory, overall_totals = analyze_project()

    grand_total_files = 0
    grand_total_lines = 0

    print("--- Analysis Results by Top-Level Directory ---")
    # Sort by top-level directory name for consistent output
    for top_dir in sorted(stats_by_directory.keys()):
        print(f"\nDirectory/File: {top_dir}")
        dir_total_files = 0
        dir_total_lines = 0
        data = stats_by_directory[top_dir]

        if 'Java' in data:
            files = data['Java']['files']
            lines = data['Java']['lines']
            print(f"  Java:       {files:>6} files, {lines:>8} lines")
            dir_total_files += files
            dir_total_lines += lines

        if 'TypeScript' in data:
            files = data['TypeScript']['files']
            lines = data['TypeScript']['lines']
            print(f"  TypeScript: {files:>6} files, {lines:>8} lines")
            dir_total_files += files
            dir_total_lines += lines

        # print(f"  ------------------------------------")
        # print(f"  Subtotal:   {dir_total_files:>6} files, {dir_total_lines:>8} lines")


    print("\n" + "="*40)
    print("--- Overall Project Totals ---")
    print("="*40)

    java_files = overall_totals.get('Java', {}).get('files', 0)
    java_lines = overall_totals.get('Java', {}).get('lines', 0)
    ts_files = overall_totals.get('TypeScript', {}).get('files', 0)
    ts_lines = overall_totals.get('TypeScript', {}).get('lines', 0)

    total_files = java_files + ts_files
    total_lines = java_lines + ts_lines

    print(f"Total Java Files:       {java_files:>6}")
    print(f"Total TypeScript Files: {ts_files:>6}")
    print("-" * 40)
    print(f"Total Java Lines:       {java_lines:>8}")
    print(f"Total TypeScript Lines: {ts_lines:>8}")
    print("-" * 40)
    print(f"GRAND TOTAL FILES:      {total_files:>6}")
    print(f"GRAND TOTAL LINES:      {total_lines:>8}")
    print("="*40)
